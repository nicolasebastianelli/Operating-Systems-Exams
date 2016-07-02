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

int main(int argc, char* argv[]) {
	int k, p, fdi, pid, fdo;
	if (argc != 5) {
		fprintf(stderr, "Numero di argomenti errato\n");
		exit(EXIT_FAILURE);
	}
	if ((k = atoi(argv[1])) > 10 || (k = atoi(argv[1])) <= 0) {
		fprintf(stderr, "k > 10 o <= 0 non è un numero\n");
		exit(EXIT_FAILURE);
	}
	if ((p = atoi(argv[2])) <= 0) {
		fprintf(stderr, "p < 0 o non è un numero\n");
		exit(EXIT_FAILURE);
	}
	if (argv[3][0] != '/') {
		fprintf(stderr, "file input non è un path assoluto\n");
		exit(EXIT_FAILURE);
	}
	if (argv[4][0] != '/') {
		fprintf(stderr, "file output non è un path assoluto\n");
		exit(EXIT_FAILURE);
	}
	if ((fdi = open(argv[3], O_RDWR | O_CREAT | O_TRUNC, 0755)) < 0) {
		fprintf(stderr, "errore nell'apertura di file input\n");
		exit(EXIT_FAILURE);
	}
    for(int l=0;l<10;l++) {
        if ((write(fdi, &l, sizeof(int))) < 0) {
            fprintf(stderr, "errore in scrittura su fileout\n");
            exit(EXIT_FAILURE);
        }
    }
    lseek(fdi, 0, SEEK_SET);
	if ((fdo = open(argv[4], O_WRONLY | O_CREAT | O_TRUNC)) < 0) {
		fprintf(stderr, "errore nell'apertura di file out\n");
		exit(EXIT_FAILURE);
	}
	int fdpipe[k][2], i = 0;
	int  buff[k];
	while (i != k) {
		if (read(fdi, &buff[k], sizeof(int)) <= 0) {
			fprintf(stderr, "numero di caratteri letti errato\n");
			exit(EXIT_FAILURE);
		}
		i++;
	}
	lseek(fdi, 0, SEEK_SET);
	for (int i = 0; i < k; i++) {
		if (pipe(fdpipe[i]) != 0) {
			fprintf(stderr, "errore nella creazione della pipe %d\n", i);
			exit(EXIT_FAILURE);
		}
		pid = fork();
		if (pid == 0) //codice figli
				{
			int  nserie,estrazione;
            char output[255];
			int n = 1;
			for (int j = 0; j <= i; j++){
				close(fdpipe[j][WRITE]);

			}
			if (read(fdi, &nserie, sizeof(int)) <= 0) {
				fprintf(stderr, "numero di caratteri letti errato\n");
				exit(EXIT_FAILURE);
			}
			printf("Figlio: %d con pid: [%d] numero estratto %d\n",i,getpid(),nserie);
			while ((read(fdpipe[i][READ], &estrazione, sizeof(int))) > 0) {
				if (estrazione == nserie) {
                     printf("Sono il partecipante %d con PID %d, ed ho vinto l'estrazione %d grazie al biglietto numero %d.\n",i,getpid(), n, nserie);
					sprintf(output,
							"Sono il partecipante con PID %d, ed ho vinto l'estrazione %d grazie al biglietto numero %d.\n",
							getpid(), n, nserie);
					if ((write(fdo, &output, strlen(output))) < 0) {
						fprintf(stderr, "errore in scrittura su fileout\n");
						exit(EXIT_FAILURE);
					}
				}
				n++;
			}
            close(fdo);
            close(fdi);
			close(fdpipe[i][READ]);
			exit(0);
		} else if (pid > 0) //codice padre
				{
			printf("Figlio: %d con pid: [%d] creato\n", i, pid);
			close(fdpipe[i][READ]);
		} else {
			fprintf(stderr, "errore nella fork\n");
			exit(EXIT_FAILURE);
		}
	}
	int status;
	int estrazione;
	char supp;
	char output[255];
	sprintf(output,
			"********************INIZIO LOTTERIA********************\n\n");
	if ((write(fdo, output, strlen(output))) <= 0) {
		fprintf(stderr, "errore in scrittura su fileout\n");
		exit(EXIT_FAILURE);
	}
	srand(getpid() * time(NULL));
	for (int i = 0; i < p; i++) {
		estrazione = rand() % k;
		printf("Padre con pid: [%d] numero estratto: %d\n",getpid(),estrazione);
		for (int j = 0; j < k; j++) {
			if ((write(fdpipe[j][WRITE], &estrazione, sizeof(int))) <= 0) {
				fprintf(stderr, "errore in scrittura sulle pipe\n");
				exit(EXIT_FAILURE);
			}
		}
		sleep(3);
	}
	for (int i = 0; i < k; i++) {
		close(fdpipe[i][WRITE]);
	}
	for (int i = 0; i < k; i++) {
		wait(&status);

		if (WIFEXITED(status)) {
			printf("Figlio terminato volontariamente con stato: %d\n",
					WEXITSTATUS(status));
		} else if (WIFSIGNALED(status)) {
			printf("Figlio terminato volontariamente con stato: %d\n",
					WEXITSTATUS(status));
		}
	}
	sprintf(output,
			"\n*********************FINE LOTTERIA*********************\n\n");
	if ((write(fdo, output, strlen(output))) <= 0) {
		fprintf(stderr, "errore in scrittura su fileout\n");
		exit(EXIT_FAILURE);
	}
	close(fdi);
	close(fdo);
		return 0;
}
