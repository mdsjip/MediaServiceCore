package com.liskovsoft.mediaserviceinterfaces;

public interface MediaItemManager {
    MediaItemDetails getMediaItemDetails(MediaItem item);
    MediaItemDetails getMediaItemDetails(String videoId);
}
