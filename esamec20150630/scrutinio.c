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

void handlerp(int sig);
int fdo;
int main(int argc, char* argv[]) {
	int fdi, votinulli=0,fdpipe[2],pid1,pid2,status;
	char N[4][20]={"Paola","Roberta","Massimo","Luca"};
	printf("Inizio programma\n\n");
	if (argc != 3) {
			fprintf(stderr, "Numero di argomenti errato\n");
			exit(EXIT_FAILURE);
		}
	if (argv[1][0] != '/') {
			fprintf(stderr, "file input non è un path assoluto\n");
			exit(EXIT_FAILURE);
		}
	if (argv[2][0] != '/') {
			fprintf(stderr, "file output non è un path assoluto\n");
			exit(EXIT_FAILURE);
		}
	if ((fdi = open(argv[1], O_RDONLY)) < 0) {
			fprintf(stderr, "errore nell'apertura di file input\n");
			exit(EXIT_FAILURE);
		}
	if ((fdo = open(argv[2], O_RDWR | O_CREAT | O_TRUNC, 0755)) < 0) {
			fprintf(stderr, "errore nell'apertura di file output\n");
			exit(EXIT_FAILURE);
		}
	if (pipe(fdpipe) != 0) {
				fprintf(stderr, "errore nella creazione della pipe\n");
				exit(EXIT_FAILURE);
			}
	pid1 = fork();
	if(pid1==0) //figlio1
	{
		close(fdpipe[READ]);
		close(1);
		dup(fdpipe[WRITE]);
		execlp("cut","cut","-d",";","-f1",argv[1],(char*) 0);
		close(fdo);
		close(fdi);
		close(fdpipe[WRITE]);
		exit(0);
	}
	else if (pid1>0) //padre
	{
		printf("[%d]: Figlio 1 con pid: [%d] creato\n",getpid(), pid1);
		pid2=fork();
		if(pid2==0) //figlio2
			{
			char buff,C[50],output[70];
			int nC[4],i=0,vN=0;
			nC[0]=0;
			nC[1]=0;
			nC[2]=0;
			nC[3]=0;
			close(fdpipe[WRITE]);
			while ((read(fdpipe[READ], &buff, sizeof(char))) > 0) {
					if(buff!='\n')
					{
						C[i]=buff;
						i++;
					}
					else
					{
						C[i]='\0';
						i=0;
						if(strcmp(C,N[0])==0){
							nC[0]++;
						}
						else if(strcmp(C,N[1])==0){
							nC[1]++;
						}
						else if(strcmp(C,N[2])==0){
							nC[2]++;
						}
						else if(strcmp(C,N[3])==0){
							nC[3]++;
						}
						else {
							vN++;
						}
					}
			}
			for(int j =0 ;j<4;j++)
			{
				sprintf(output,"%s:%d\n",N[j],nC[j]);
			if ((write(fdo, &output, strlen(output))) < 0) {
									fprintf(stderr, "errore in scrittura su fileout\n");
									exit(EXIT_FAILURE);
								}
			}
			kill(getppid(),SIGUSR1);
			printf("[%d]: Numero voti nulli: %d\n",getpid(),vN);
			close(fdo);
			close(fdi);
			close(fdpipe[READ]);
			exit(0);
			}
			else if (pid2>0) //padre
			{
				close(fdpipe[READ]);
				close(fdpipe[WRITE]);
				signal(SIGUSR1,handlerp);
				printf("[%d]: Figlio 2 con pid: [%d] creato\n",getpid(), pid2);
				pause();
				for (int i = 0; i < 2; i++) {
						wait(&status);
						if (WIFEXITED(status)) {
							printf("[%d]: Figlio terminato volontariamente con stato: %d\n",getpid(),
									WEXITSTATUS(status));
						} else if (WIFSIGNALED(status)) {
							printf("[%d]: Figlio terminato volontariamente con stato: %d\n",getpid(),
									WEXITSTATUS(status));
						}
					}
				close(fdo);
				close(fdi);
				printf("\n[%d]:Terminazione programma\n",getpid());
				exit(0);
			}
			else{  //errore
				fprintf(stderr, "errore nella fork 1\n");
							exit(EXIT_FAILURE);
				}

	}
	else{  //errore
		fprintf(stderr, "errore nella fork 2\n");
					exit(EXIT_FAILURE);
	}

	return 0;

}

void handlerp(int sig) {
	char c;
	lseek(fdo,0,SEEK_SET);
	while ((read(fdo, &c, sizeof(char))) > 0) {
		printf("%c",c);
	}
	return;
}
