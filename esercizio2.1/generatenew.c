#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char* argv[]) {
	int pid, status, n;
	n = atoi(argv[1]);
	if (argc != 2) {
		fprintf(stderr, "Numero di argomenti errato\n");
		exit(EXIT_FAILURE);
	}
	for (int i = 0; i < n; i++) {
		pid = fork();
		if (pid == 0) {
			printf("Sono il figlio [PID:%d]\n", getpid());
			exit(1);
		} else if (pid > 0) {	}
		else {
			fprintf(stderr, "creazione processo non avvenuta con successo");
			exit(0);
		}
	}

	for (int i = 0; i < n; i++) {
		pid = wait(&status);
		if (WIFEXITED(status))
			printf("Figlio %d terminato volontariamente con stato %d\n", pid,
					WEXITSTATUS(status));
		else if (WIFSIGNALED(status))
			printf("Figlio %d terminato involontariamente con stato %d\n", pid,
					WEXITSTATUS(status));
	}
	return 0;
}
