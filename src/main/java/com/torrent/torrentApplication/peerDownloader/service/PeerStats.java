package com.torrent.torrentApplication.peerDownloader.service;

public class PeerStats {
    private int successCount;
    private int failureCount;
    private long totalResponseTime;

    public synchronized void recordSuccess(long responseTime) {
        successCount++;
        totalResponseTime += responseTime;
    }

    public synchronized void recordFailure() {
        failureCount++;
    }

    public synchronized int getSuccessCount() {
        return successCount;
    }

    public synchronized int getFailureCount() {
        return failureCount;
    }

    public synchronized long getAverageResponseTime() {
        if (successCount == 0) {
            return Long.MAX_VALUE; // No successful downloads yet
        }
        return totalResponseTime / successCount;
    }

    public synchronized double getScore() {

        double score = 100;

        // Reward successful downloads
        score += successCount * 5;

        // Penalize failures
        score -= failureCount * 20;

        // Penalize higher response times
        if (successCount > 0) {
            score -= getAverageResponseTime() / 10.0;
        }

        return score;
    }

    @Override
    public synchronized String toString() {
        return "PeerStats{" +
                "successCount=" + successCount +
                ", failureCount=" + failureCount +
                ", averageResponseTime=" + getAverageResponseTime() +
                ", score=" + getScore() +
                '}';
    }
}
