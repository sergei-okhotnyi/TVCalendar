package dev.okhotny.TVCalendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;

import java.nio.ByteBuffer;

/**
 * Created by sergii on 2014-05-05.
 */
public class DiskBitmapCache extends DiskBasedCache implements ImageLoader.ImageCache {
    private static final int MAX_BYTE_SIZE = 25 * 1024 * 1024;
    private LruCache<String, Bitmap> memory = new LruCache<String, Bitmap>(MAX_BYTE_SIZE);

    public DiskBitmapCache(Context context) {
        super(context.getCacheDir(), 5 * MAX_BYTE_SIZE);
    }

    public Bitmap getBitmap(String url) {
        Bitmap bitmap = memory.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        Entry entry = get(url);
        if (entry != null) {
            try {
                bitmap = BitmapFactory.decodeByteArray(entry.data, 0, entry.data.length);
                memory.put(url, bitmap);
                return bitmap;
            } catch (Exception ignore) {
            } catch (Error ignored) {
            }
        }
        return null;
    }

    public void putBitmap(String url, Bitmap bitmap) {
        try {
            final Entry entry = new Entry();
            ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
            bitmap.copyPixelsToBuffer(buffer);
            entry.data = buffer.array();
            put(url, entry);
            memory.put(url, bitmap);
        } catch (Exception ignore) {
        } catch (Error ignored) {
        }
    }

}
