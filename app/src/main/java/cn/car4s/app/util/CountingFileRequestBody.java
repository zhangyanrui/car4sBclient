//package cn.car4s.app.util;
//
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.internal.Util;
//import okio.BufferedSink;
//import okio.Okio;
//import okio.Source;
//
//import java.io.File;
//import java.io.IOException;
//
///**
// * Description:
// * Author: Alex
// * Email: xuebo.chang@langtaojin.com
// * Time: 2015/4/22.
// */
//public class CountingFileRequestBody extends RequestBody {
//
//    private static final int SEGMENT_SIZE = 2048; // okio.Segment.SIZE
//
//    private final File file;
//    private final ProgressListener listener;
//    private final String contentType;
//
//    public CountingFileRequestBody(File file, String contentType, ProgressListener listener) {
//        this.file = file;
//        this.contentType = contentType;
//        this.listener = listener;
//    }
//
//    @Override
//    public long contentLength() {
//        return file.length();
//    }
//
//    @Override
//    public MediaType contentType() {
//        return MediaType.parse(contentType);
//    }
//
//    @Override
//    public void writeTo(BufferedSink sink) throws IOException {
//        Source source = null;
//        try {
//            source = Okio.source(file);
//            long total = 0;
//            long read;
//
//            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
//                total += read;
//                sink.flush();
//                this.listener.transferred(total);
//
//            }
//        } finally {
//            Util.closeQuietly(source);
//        }
//    }
//
//    public interface ProgressListener {
//        void transferred(long num);
//    }
//
//}