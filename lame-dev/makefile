PKG_CONFIG=pkg-config

install:
	gcc lame.text.c libmp3lame.so -o lame.bin $(shell $(PKG_CONFIG) --cflags --libs lame)


