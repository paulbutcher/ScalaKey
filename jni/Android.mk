LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := ripple
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := ripple.cpp
LOCAL_LDLIBS    := -llog -lGLESv1_CM

include $(BUILD_SHARED_LIBRARY)
