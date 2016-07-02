#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char* argv[]) {
	int pid, pidf, status;
	char cmd[255];
	int len;
	if (argc < 2) {
		fprintf(stderr, "numero di argomenti non valido\n");
		exit(EXIT_FAILURE);
	}
	for (int i = 1; i < argc; i++) {
		pid = fork();
		if (pid == 0) { // figlio
			pidf = fork();
			if (pidf == 0) //nipote
					{
				len = strlen(argv[i]);
				strncpy(cmd, argv[i], len - 2);
				cmd[len+4+4+len-2]='\0';
				printf("gcc %s -o %s\n",argv[i],cmd);
				execlp("gcc","gcc" ,argv[i],"-o",cmd, (char*) 0);
				exit(0);
			} else if (pidf > 0) //figlio
					{
				wait(&status);
				len = strlen(argv[i]);
				strcpy(cmd,"./");
				strncpy(cmd+2,argv[i],len-2);
				printf("%s\n",cmd);
				execlp(cmd, cmd, (char*) 0);
				exit(1);
			} else {
				fprintf(stderr, "creazione del nipote non effettuata\n");
				exit(EXIT_FAILURE);
			}
		} else if (pid < 0) {
			fprintf(stderr, "creazione del figlio non effettuata\n");
			exit(EXIT_FAILURE);
		}
	}
	if (pid > 0) { //padre
		for (int i = 1; i < argc; i++) {
			wait(&status);
		}
		exit(0);
	}
	return 0;
}
