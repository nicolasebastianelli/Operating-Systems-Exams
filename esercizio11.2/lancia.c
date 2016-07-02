#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int pid;
void p_handler() {
	kill(pid, SIGKILL);
}

int main(int argc, char* argv[]) {
	int timeout, status, fd;
	if (argc != 4) {
		fprintf(stderr, "Numero di argomenti errato\n");
		exit(EXIT_FAILURE);
	}
	if(strlen(argv[3])!=1 || (argv[3][0]!='a' && argv[3][0]!='d')){
		fprintf(stderr, "valore di tipo_op inserito non valido\n");
				exit(EXIT_FAILURE);
	}
	if ((timeout = atoi(argv[2])) <= 0) {
		fprintf(stderr, "Non è un numero\n");
		exit(EXIT_FAILURE);
	}
	if ((fd = open(argv[1], O_RDWR | O_CREAT)) < 0) {
		fprintf(stderr, "errore apertura file\n");
		exit(EXIT_FAILURE);
	}
	pid = fork();
	if (pid == 0) //codice figlio
			{
		close(1);
		dup(fd);
		execlp("java", "java", "-jar", "file.jar", (char*) 0);
		exit(1);
	} else if (pid > 0) { //codice padre
		char buff;
		signal(SIGALRM, &p_handler);
		alarm(timeout);
		wait(&status);
		if(WIFEXITED(status)){
			printf("Figlio:%d terminato volontatiamente con stato %d\n",pid,WEXITSTATUS(status));
		}
		else if (WIFSIGNALED(status)){
			printf("Figlio:%d terminato involontatiamente con stato %d\n",pid,WEXITSTATUS(status));
		}
		if ((lseek(fd, 0, SEEK_SET)) < 0) {
			fprintf(stderr, "errore nella lseek");
		}
		printf("Ricerca effettuata per: %s\n",argv[3]);
		int supp;
		while ((supp=read(fd, &buff, sizeof(char))) != 0) {
			if (buff != argv[3][0])
				while ((supp=read(fd, &buff, sizeof(char)))) {
					if(supp==0||buff=='\n')
						break;
				}
			else{
				printf("%c",buff);
				while ((supp=read(fd, &buff, sizeof(char)))){
					if(supp==0||buff=='\n')
						break;
					printf("%c",buff);
				}printf("\n");
			}
		}

	} else {
		fprintf(stderr, "Errore nella fork\n");
		exit(EXIT_FAILURE);
	}
	return 0;
}

