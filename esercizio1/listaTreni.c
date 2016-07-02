#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char* argv[]) {

	if (argc < 2) {
		fprintf(stderr, "Numero argomenti errato\n");
		exit(EXIT_FAILURE);
	}
	for (int i = 1; i < argc; i++) {
		if (strlen(argv[i]) != 6) {
			fprintf(stderr, "Lunghezza dell'argomento %d errata\n", i);
			exit(EXIT_FAILURE);
		}
	}
	printf("IC\n\n");
	for (int i = 1; i < argc; i++) {
		if (argv[i][0] == 'I')
			if (argv[i][1] == 'C')
				printf("%s\n", argv[i]);
	}
	printf("\n");
	printf("FR\n\n");
	for (int i = 1; i < argc; i++) {
		if (argv[i][0] == 'F')
			if (argv[i][1] == 'R')
				printf("%s\n", argv[i]);
	}
	printf("\n");
	printf("RG\n\n");
	for (int i = 1; i < argc; i++) {
		if (argv[i][0] == 'R')
			if (argv[i][1] == 'G')
				printf("%s\n", argv[i]);
	}
	exit(0);

}

