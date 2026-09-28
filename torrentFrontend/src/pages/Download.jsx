import "./Download.css";
import { useId, useState } from "react";
import api from "../services/api";

const formatBytes = (bytes) => {
    if (!bytes && bytes !== 0) return "—";
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(0)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(2)} MB`;
};

function Download() {
    const shareCodeId = useId();

    const [shareCode, setShareCode] = useState("");
    const [file, setFile] = useState(null);
    const [progress, setProgress] = useState(0);

    const [isSearching, setIsSearching] = useState(false);
    const [isDownloading, setIsDownloading] = useState(false);
    const [searchError, setSearchError] = useState("");
    const [downloadError, setDownloadError] = useState("");

    const handleSearch = async (event) => {
        event.preventDefault();

        const trimmedCode = shareCode.trim();
        if (!trimmedCode) {
            setSearchError("Enter a share code first.");
            return;
        }

        setIsSearching(true);
        setSearchError("");
        setDownloadError("");
        setFile(null);
        setProgress(0);

        try {
            const response = await api.get(`/api/files/${trimmedCode}`);
            setFile(response.data);
        } catch (error) {
            setSearchError("No file found for that share code.");
            console.error(error);
        } finally {
            setIsSearching(false);
        }
    };

    const handleDownload = async () => {
        setIsDownloading(true);
        setDownloadError("");
        setProgress(0);

        try {
            const response = await api.post(`/download/${shareCode}`);
            const fileId = response.data;

            const fileResponse = await api.get(`/download/file/${fileId}`, {
                responseType: "blob",
                onDownloadProgress: (progressEvent) => {
                    if (progressEvent.total) {
                        const percent = Math.round(
                            (progressEvent.loaded / progressEvent.total) * 100
                        );
                        setProgress(percent);
                    }
                }
            });

            const url = window.URL.createObjectURL(fileResponse.data);

            const link = document.createElement("a");
            link.href = url;
            link.download = file.fileName || "final.mp4";

            document.body.appendChild(link);
            link.click();
            link.remove();

            window.URL.revokeObjectURL(url);

            setProgress(100);
        } catch (error) {
            setDownloadError("Download failed. Please try again.");
            console.error(error);
            setProgress(0);
        } finally {
            setIsDownloading(false);
        }
    };

    const pieceCount = file ? Math.min(file.totalPieces || 0, 32) : 0;

    return (
    <div className="download-page">

        <div className="download-container">

            {/* Page Header */}

            <div className="download-header">

                <div>
                    <p className="download-eyebrow">
                        FILE TRANSFER
                    </p>

                    <h1>
                        Download File
                    </h1>

                    <p className="download-description">
                        Enter a share code to find and download a file
                        from the network.
                    </p>
                </div>

            </div>


            {/* Search Section */}

            <section className="download-panel">

                <div className="panel-title">

                    <div className="title-icon">
                        ↓
                    </div>

                    <div>
                        <h2>
                            Find a File
                        </h2>

                        <p>
                            Enter the share code provided by the file owner.
                        </p>
                    </div>

                </div>


                <form
                    className="search-form"
                    onSubmit={handleSearch}
                >

                    <div className="input-group">

                        <label htmlFor={shareCodeId}>
                            Share Code
                        </label>

                        <div className="input-wrapper">

                            <input
                                id={shareCodeId}
                                type="text"
                                inputMode="text"
                                autoComplete="off"
                                spellCheck="false"
                                placeholder="e.g. 7F3K-9QZP"
                                value={shareCode}
                                onChange={(e) => {
                                    setShareCode(e.target.value);

                                    if (searchError) {
                                        setSearchError("");
                                    }
                                }}
                                aria-invalid={Boolean(searchError)}
                                aria-describedby={
                                    searchError
                                        ? `${shareCodeId}-error`
                                        : undefined
                                }
                            />

                            <button
                                type="submit"
                                className="find-button"
                                disabled={isSearching}
                                aria-busy={isSearching}
                            >
                                {isSearching
                                    ? "Searching..."
                                    : "Find File"}
                            </button>

                        </div>

                    </div>

                    {searchError && (
                        <p
                            className="field-error"
                            id={`${shareCodeId}-error`}
                            role="alert"
                        >
                            {searchError}
                        </p>
                    )}

                </form>

            </section>


            {/* File Information */}

            {file && (

                <section className="download-panel file-panel">

                    <div className="file-panel-header">

                        <div className="file-heading">

                            <div className="file-icon">
                                {file.fileName
                                    ?.split(".")
                                    .pop()
                                    ?.toUpperCase()
                                    .slice(0, 4)}
                            </div>

                            <div>

                                <p className="file-label">
                                    FILE FOUND
                                </p>

                                <h2>
                                    {file.fileName}
                                </h2>

                            </div>

                        </div>

                        <span className="available-badge">
                            Available
                        </span>

                    </div>


                    {/* File Details */}

                    <div className="file-details">

                        <div className="detail-item">

                            <span>
                                File Size
                            </span>

                            <strong>
                                {formatBytes(file.fileSize)}
                            </strong>

                        </div>


                        <div className="detail-item">

                            <span>
                                Piece Size
                            </span>

                            <strong>
                                {formatBytes(file.pieceSize)}
                            </strong>

                        </div>


                        <div className="detail-item">

                            <span>
                                Total Pieces
                            </span>

                            <strong>
                                {file.totalPieces}
                            </strong>

                        </div>


                        <div className="detail-item">

                            <span>
                                Share Code
                            </span>

                            <strong className="detail-code">
                                {file.shareCode}
                            </strong>

                        </div>

                    </div>


                    {/* Piece Map */}

                    {pieceCount > 0 && (

                        <div className="piece-section">

                            <div className="piece-header">

                                <span>
                                    Download Progress
                                </span>

                                <span>
                                    {progress}%
                                </span>

                            </div>

                            <div
                                className="piece-map"
                                style={{
                                    "--piece-count": pieceCount
                                }}
                                aria-hidden="true"
                            >

                                {Array.from({
                                    length: pieceCount
                                }).map((_, i) => {

                                    const threshold =
                                        ((i + 1) / pieceCount) * 100;

                                    return (
                                        <span
                                            key={i}
                                            className={`piece ${
                                                progress >= threshold
                                                    ? "piece-filled"
                                                    : ""
                                            }`}
                                        />
                                    );

                                })}

                            </div>

                        </div>

                    )}


                    {downloadError && (

                        <p
                            className="field-error download-error"
                            role="alert"
                        >
                            {downloadError}
                        </p>

                    )}


                    {/* Download Button */}

                    <button
                        type="button"
                        className="download-button"
                        onClick={handleDownload}
                        disabled={isDownloading}
                        aria-busy={isDownloading}
                    >
                        {isDownloading
                            ? `Downloading... ${progress}%`
                            : "Download File"}
                    </button>


                    {/* Progress Bar */}

                    {isDownloading && (

                        <div className="progress-section">

                            <div className="progress-info">

                                <span>
                                    Downloading file
                                </span>

                                <strong>
                                    {progress}%
                                </strong>

                            </div>

                            <div
                                className="progress-track"
                                role="progressbar"
                                aria-valuenow={progress}
                                aria-valuemin={0}
                                aria-valuemax={100}
                            >

                                <div
                                    className="progress-fill"
                                    style={{
                                        width: `${progress}%`
                                    }}
                                />

                            </div>

                        </div>

                    )}

                </section>

            )}

        </div>

    </div>
);
}

export default Download;