import "./Home.css";
import { Link, useNavigate } from "react-router-dom";
import { useEffect, useMemo, useState } from "react";
import api from "../services/api";

function Home() {
  const navigate = useNavigate();

  const [sharedFiles, setSharedFiles] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [copiedCode, setCopiedCode] = useState("");

  const username = localStorage.getItem("username");

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/login");
      return;
    }

    const fetchFiles = async () => {
      try {
        const response = await api.get("/api/files/my-files");
        setSharedFiles(response.data);
      } catch (error) {
        console.error("Failed to load shared files:", error);
      }
    };

    fetchFiles();
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("username");

    navigate("/login");
  };

  const formatSize = (bytes) => {
    if (!bytes || bytes === 0) {
      return "0 B";
    }

    const units = ["B", "KB", "MB", "GB", "TB"];
    const index = Math.floor(Math.log(bytes) / Math.log(1024));

    return `${(bytes / Math.pow(1024, index)).toFixed(1)} ${units[index]}`;
  };

  const getFileType = (fileName) => {
    const extension = fileName?.split(".").pop()?.toUpperCase();

    return extension || "FILE";
  };

  const handleCopy = async (shareCode) => {
    try {
      await navigator.clipboard.writeText(shareCode);

      setCopiedCode(shareCode);

      setTimeout(() => {
        setCopiedCode("");
      }, 1500);
    } catch (error) {
      console.error("Failed to copy:", error);
    }
  };

  const filteredFiles = useMemo(() => {
    return sharedFiles.filter((file) =>
      file.fileName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      file.shareCode?.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [sharedFiles, searchTerm]);

  const totalPieces = sharedFiles.reduce(
    (total, file) => total + (file.totalNoOfPieces || 0),
    0
  );

  const totalStorage = sharedFiles.reduce(
    (total, file) => total + (file.fileSize || 0),
    0
  );

  return (
    <div className="dashboard">

      {/* Sidebar */}

      <aside className="sidebar">

        <div className="brand">
          <div className="brand-mark">T</div>
          <div>
            <h1>Torrent</h1>
            <span>File Sharing</span>
          </div>
        </div>

        <nav className="sidebar-nav">

          <Link to="/" className="nav-item active">
            <span>▦</span>
            Dashboard
          </Link>

          <a href="#shared-files" className="nav-item">
            <span>□</span>
            Shared Files
          </a>

          <Link to="/download" className="nav-item">
            <span>↓</span>
            Downloads
          </Link>

          <Link to="/about" className="nav-item">
            <span>ⓘ</span>
            About
          </Link>

        </nav>

        <div className="sidebar-bottom">

          <Link to="/share" className="share-button">
            <span>+</span>
            Share File
          </Link>

          <div className="user-section">

            <div className="user-avatar">
              {username?.charAt(0).toUpperCase() || "U"}
            </div>

            <div className="user-info">
              <strong>{username || "User"}</strong>
              <span>Logged in</span>
            </div>

            <button
              className="logout-button"
              onClick={handleLogout}
              title="Logout"
            >
              ↪
            </button>

          </div>

        </div>

      </aside>


      {/* Main Content */}

      <main className="main-content">

        {/* Header */}

        <header className="dashboard-header">

          <div>
            <p className="eyebrow">OVERVIEW</p>

            <h2>
              Welcome back, {username || "User"}
            </h2>

            <p className="header-description">
              Manage your shared files and track your file activity.
            </p>
          </div>

          <Link to="/share" className="header-share-button">
            + Share File
          </Link>

        </header>


        {/* Statistics */}

        <section className="stats-grid">

          <div className="stat-card">

            <div className="stat-icon">
              □
            </div>

            <div>
              <span className="stat-label">
                Shared Files
              </span>

              <strong className="stat-value">
                {sharedFiles.length}
              </strong>
            </div>

          </div>


          <div className="stat-card">

            <div className="stat-icon">
              #
            </div>

            <div>
              <span className="stat-label">
                Total Pieces
              </span>

              <strong className="stat-value">
                {totalPieces}
              </strong>
            </div>

          </div>


          <div className="stat-card">

            <div className="stat-icon">
              ◇
            </div>

            <div>
              <span className="stat-label">
                Storage Used
              </span>

              <strong className="stat-value">
                {formatSize(totalStorage)}
              </strong>
            </div>

          </div>

        </section>


        {/* Shared Files */}

        <section className="files-panel" id="shared-files">

          <div className="panel-header">

            <div>
              <h3>Shared Files</h3>

              <p>
                Files you have shared with the network
              </p>
            </div>

            <div className="file-search">

              <span>⌕</span>

              <input
                type="text"
                placeholder="Search files..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />

            </div>

          </div>


          {/* Table Header */}

          {filteredFiles.length > 0 && (
            <div className="file-table-header">

              <span>FILE</span>
              <span>SIZE</span>
              <span>PIECES</span>
              <span>SHARE CODE</span>
              <span>STATUS</span>

            </div>
          )}


          {/* Files */}

          <div className="file-list">

            {filteredFiles.length === 0 ? (

              <div className="empty-state">

                <div className="empty-icon">
                  □
                </div>

                <h4>
                  {searchTerm
                    ? "No matching files"
                    : "No files shared yet"}
                </h4>

                <p>
                  {searchTerm
                    ? "Try searching with another file name or share code."
                    : "Share your first file to see it appear here."}
                </p>

                {!searchTerm && (
                  <Link to="/share" className="empty-button">
                    Share Your First File
                  </Link>
                )}

              </div>

            ) : (

              filteredFiles.map((file) => (

                <div className="file-row" key={file.fileId}>

                  <div className="file-name">

                    <div className="file-type">
                      {getFileType(file.fileName)}
                    </div>

                    <div>
                      <strong>
                        {file.fileName}
                      </strong>

                      <span>
                        ID #{file.fileId}
                      </span>
                    </div>

                  </div>


                  <div className="file-size">
                    {formatSize(file.fileSize)}
                  </div>


                  <div className="file-pieces">
                    {file.totalNoOfPieces || 0}
                  </div>


                  <div className="share-code">

                    <code>
                      {file.shareCode}
                    </code>

                    <button
                      onClick={() => handleCopy(file.shareCode)}
                      className="copy-button"
                    >
                      {copiedCode === file.shareCode
                        ? "Copied"
                        : "Copy"}
                    </button>

                  </div>


                  <div>
                    <span className="status-badge">
                      Shared
                    </span>
                  </div>

                </div>

              ))

            )}

          </div>

        </section>


        {/* Downloads */}

        <section className="downloads-panel">

          <div className="panel-header">

            <div>
              <h3>Recent Downloads</h3>

              <p>
                Your downloaded files will appear here.
              </p>
            </div>

            <Link to="/download">
              View Downloads →
            </Link>

          </div>

          <div className="download-empty">

            <div className="download-empty-icon">
              ↓
            </div>

            <div>
              <strong>No download history yet</strong>

              <p>
                Downloaded files will be displayed here once
                download history is connected.
              </p>
            </div>

          </div>

        </section>

      </main>

    </div>
  );
}

export default Home;