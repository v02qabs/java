#include <stdio.h>
void push()
{
	int c = 10;
	while(1)
	{
		printf("Hello.\n");
		c = c - 1;
		if(c == 0)
			break;
	}

		
}


void pop(int a)
{
	printf("pop");
	for(int i = 0; i<=a; i++)
	{
		printf("%d\n", i);
	}

}

int main()
{
	pop(5);

	push();
	return 0;

}

