import "./ShareFile.css";
import React, { useState } from "react";
import api from "../services/api";


const ShareFile = () => {
    const [userId, setUserId] = useState("");
    const [filePath, setFilePath] = useState("");

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState("");
    const [error, setError] = useState("");

    const handleShare = async (e) => {
        e.preventDefault();

        setSuccess("");
        setError("");

        if (!userId.trim()) {
            setError("Please enter your User ID.");
            return;
        }

        if (!filePath.trim()) {
            setError("Please enter the file path.");
            return;
        }

        try {
            setLoading(true);

            const response = await api.post("/api/peer/share", {
                userId: Number(userId),
                filePath: filePath.trim()
            });

            setSuccess(
                response.data || "File shared successfully."
            );

            setFilePath("");

        } catch (err) {
            console.error("Error sharing file:", err);

            if (err.response?.data) {
                setError(
                    typeof err.response.data === "string"
                        ? err.response.data
                        : "Failed to share the file."
                );
            } else {
                setError(
                    "Unable to connect to the server. Please try again."
                );
            }

        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="file-share-page">

            <div className="file-share-wrapper">

                {/* Page Header */}
                <div className="file-share-heading">
                    <div className="share-icon">
                        <span>↗</span>
                    </div>

                    <div>
                        <h1>Share File</h1>
                        <p>
                            Add a file to the peer-to-peer network
                        </p>
                    </div>
                </div>

                {/* Share Form */}
                <div className="share-form-card">

                    <div className="card-title">
                        <h2>Share a new file</h2>
                        <p>
                            Provide the file information below to
                            start sharing.
                        </p>
                    </div>

                    <form onSubmit={handleShare}>

                        {/* User ID */}
                        <div className="input-group">

                            <label htmlFor="userId">
                                User ID
                            </label>

                            <input
                                id="userId"
                                type="number"
                                value={userId}
                                onChange={(e) =>
                                    setUserId(e.target.value)
                                }
                                placeholder="Enter your user ID"
                                disabled={loading}
                            />

                        </div>

                        {/* File Path */}
                        <div className="input-group">

                            <label htmlFor="filePath">
                                File Path
                            </label>

                            <input
                                id="filePath"
                                type="text"
                                value={filePath}
                                onChange={(e) =>
                                    setFilePath(e.target.value)
                                }
                                placeholder="/path/to/your/file"
                                disabled={loading}
                            />

                            <span className="input-hint">
                                Enter a file path accessible by the
                                backend server.
                            </span>

                        </div>

                        {/* Information */}
                        <div className="info-box">

                            <div className="info-symbol">
                                i
                            </div>

                            <div>
                                <strong>How file sharing works</strong>

                                <p>
                                    Your file will be registered,
                                    divided into 512 KB pieces, and
                                    announced to the peer network.
                                </p>
                            </div>

                        </div>

                        {/* Success */}
                        {success && (
                            <div className="message success">
                                <div className="message-icon">
                                    ✓
                                </div>

                                <span>{success}</span>
                            </div>
                        )}

                        {/* Error */}
                        {error && (
                            <div className="message error">
                                <div className="message-icon">
                                    !
                                </div>

                                <span>{error}</span>
                            </div>
                        )}

                        {/* Submit */}
                        <button
                            type="submit"
                            className="share-submit-btn"
                            disabled={loading}
                        >

                            {loading ? (
                                <>
                                    <span className="loading-spinner"></span>
                                    Sharing...
                                </>
                            ) : (
                                <>
                                    <span>↗</span>
                                    Share File
                                </>
                            )}

                        </button>

                    </form>

                </div>

                {/* Process Section */}
                <div className="sharing-process">

                    <h2>File sharing process</h2>

                    <div className="process-grid">

                        <div className="process-step">

                            <div className="step-number">
                                1
                            </div>

                            <div>
                                <h3>Register</h3>
                                <p>
                                    File metadata and SHA-256 hash
                                    are generated and registered.
                                </p>
                            </div>

                        </div>

                        <div className="process-step">

                            <div className="step-number">
                                2
                            </div>

                            <div>
                                <h3>Split</h3>
                                <p>
                                    The file is divided into smaller
                                    512 KB pieces.
                                </p>
                            </div>

                        </div>

                        <div className="process-step">

                            <div className="step-number">
                                3
                            </div>

                            <div>
                                <h3>Announce</h3>
                                <p>
                                    Your peer announces the available
                                    pieces to the network.
                                </p>
                            </div>

                        </div>

                    </div>

                </div>

            </div>

        </div>
    );
};

export default ShareFile;