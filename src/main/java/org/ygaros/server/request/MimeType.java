package org.ygaros.server.request;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MimeType {
    private static final String APPLICATION = "application";
    private static final String IMAGE = "image";
    private static final String TEXT = "text";
    private static final String MULTIPART = "multipart";

    public static final MimeType CSS = new MimeType(TEXT, "css");
    public static final MimeType PLAIN = new MimeType(TEXT, "plain");
    public static final MimeType HTML = new MimeType(TEXT, "html");
    public static final MimeType JS = new MimeType(TEXT, "javascript");

    public static final MimeType JSON = new MimeType(APPLICATION, "json");
    public static final MimeType OCTET = new MimeType(APPLICATION, "octet-stream");
    public static final MimeType OGG = new MimeType(APPLICATION, "ogg");

    public static final MimeType APNG = new MimeType(IMAGE, "apng");
    public static final MimeType PNG = new MimeType(IMAGE, "png");
    public static final MimeType AVIF = new MimeType(IMAGE, "avif");
    public static final MimeType GIF = new MimeType(IMAGE, "gif");
    public static final MimeType JPEG = new MimeType(IMAGE, "jpeg");
    public static final MimeType SVG = new MimeType(IMAGE, "svg+xml");
    public static final MimeType WEBP = new MimeType(IMAGE, "webp");

    public static final MimeType FORM_DATA = new MimeType(MULTIPART, "form-data");

    private final String type;
    private final String subType;

    public MimeType(String type){
        this.type = type;
        this.subType = "*";
    }

    public MimeType(String type, String subType) {
        this.type = type;
        this.subType = subType;
    }

    @Override
    public String toString() {
        return this.type.concat("/").concat(this.subType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MimeType mimeType = (MimeType) o;
        return Objects.equals(type, mimeType.type) && Objects.equals(subType, mimeType.subType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, subType);
    }

    public static class MimeTypeUtils{
        public static Map<String, MimeType> map = new HashMap<>(15);
        static{
            for (Field declaredField : MimeType.class.getDeclaredFields()) {
                if(Modifier.isStatic(declaredField.getModifiers()) &&
                    Modifier.isPublic(declaredField.getModifiers())){
                    declaredField.setAccessible(true);
                    try {
                        map.put(declaredField.getName().toLowerCase(Locale.ROOT), (MimeType) declaredField.get(null));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            map.put("scss", CSS);
            map.put("jpg", JPEG);
        }
    }
}
