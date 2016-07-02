#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <time.h>

#define READ 0
#define WRITE 1

void handlerf(int sig);
void handlerp(int sig);
char comando[100];
int fdp[2];

int main(int argc, char* argv[]) {
	printf("[%d]: Inizio esecuzione\n",getpid());
	char cstop, cecc, buff;
	int fd1, pid1, pid0,status;
	if (argc != 5) {
		fprintf(stderr, "Numero di argomenti errato\n");
		exit(EXIT_FAILURE);
	}
	if (strlen(argv[3]) != 1) {
		fprintf(stderr, "cstop non è un carattere\n");
		exit(EXIT_FAILURE);
	}
	if (strlen(argv[4]) != 1) {
		fprintf(stderr, "cecc non è un carattere\n");
		exit(EXIT_FAILURE);
	}
	cstop = argv[3][0];
	strcpy(comando, argv[2]);
	cecc = argv[4][0];
	if (pipe(fdp) != 0) {
		fprintf(stderr, "errore nella creazione della pipe\n");
		exit(EXIT_FAILURE);
	}
	pid0 = getpid();
	pid1 = fork();
	if (pid1 == 0) // figlio
			{
		signal(SIGUSR1, handlerf);
		close(fdp[READ]);
		if ((fd1 = open(argv[1], O_RDONLY)) < 0) {
			fprintf(stderr, "errore nell'apertura di f da parte di P1\n");
			exit(EXIT_FAILURE);
		}
		while ((read(fd1, &buff, sizeof(char))) > 0) {
			if ((write(fdp[WRITE], &buff, sizeof(char))) < 0) {
				fprintf(stderr, "Errore nella scrittura sulla pipe");
				exit(EXIT_FAILURE);
			}
		}
	} else if (pid1 > 0) //padre
			{
		printf("[%d]: figlio P1 creato con pid: %d\n",getpid(),pid1);
		signal(SIGQUIT,handlerp);
		close(fdp[WRITE]);
		printf("[%d]: ",pid0);
		while ((read(fdp[READ], &buff, sizeof(char))) > 0) {
			if (buff != cecc && buff != cstop)
				printf("%c",buff);
			else if(buff== cecc)
			{
				kill(pid1,SIGUSR1);
				break;
			}
			else{
				close(fdp[READ]);
				kill(pid0,SIGQUIT);
			}
		}
		close(fdp[READ]);
		wait(&status);
		if (WIFEXITED(status)) {
					printf("\n[%d]: Figlio terminato volontariamente con stato: %d\n",getpid(),
							WEXITSTATUS(status));
				} else if (WIFSIGNALED(status)) {
					printf("\n[%d]: Figlio terminato volontariamente con stato: %d\n",getpid(),
							WEXITSTATUS(status));
				}
	} else {
		fprintf(stderr, "errore nella fork\n");
		exit(EXIT_FAILURE);
	}
	printf("[%d]: Fine esecuzione\n",getpid());
	return 0;
}

void handlerf(int sig) {
	close(fdp[WRITE]);
	char esec[100]="./";
	strcat(esec, comando);
	execlp(esec, esec, (char*) 0);
	exit(0);
	return;
}
void handlerp(int sig) {
	printf("\n[%d]: Terminazione forzata da cstop\n",getpid());
	kill(0,SIGKILL);
	return;
}
