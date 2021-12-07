package org.techtown.archivebook;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

// rest api 결과 없을 경우 처리 클래스
public class NullOnEmptyConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(ResponseBody value) throws IOException {
                if (value.contentLength() == 0) {
                    return null;
                }
                return delegate.convert(value);
            }
        };
    }
}
