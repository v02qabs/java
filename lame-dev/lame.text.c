#include <lame/lame.h>
#include <stdio.h>

#pragma comment (lib, "libmp3lame.so")

int main()
{
	lame_global_flags* flags;
	flags = lame_init();
	if(!flags)
		puts("NG.");

	if(flags)
		puts("OK");

	return 0;
}

