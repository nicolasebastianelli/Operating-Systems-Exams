#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

int pid1,pid2,pipe1[2],pipe2[2],fd_in,fine_lettura=0;



int main(int argc, char* argv[]){
    int pid[2], i;
    if (argc != 3) {
        fprintf(stderr, "Numero di argomenti errato\n");
        fprintf(stderr, "Usage:\n\t%s file_in file_out\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    if(pipe(pipe1)<0){
    	perror("P0: errore nella creazione della pipe1");
    	exit(EXIT_FAILURE);
    }
    if(pipe(pipe2)<0){
        	perror("P0: errore nella creazione della pipe2");
        	exit(EXIT_FAILURE);
        }
    pid1= fork();
    if (pid1<0)                    //errore
    {
    	fprintf(stderr, "P0: Errore nella fork");
    	            exit(EXIT_FAILURE);
    }
    else if(pid1>0)                //padre
    {
    	pid2 = fork();    	 	
    	if(pid2<0)                //errore
    	{
    		fprintf(stderr, "P0: Errore nella fork");
    		    	            exit(EXIT_FAILURE);
    	}
    	else if(pid2 > 0)        //padre
    	{ 
    		close(pipe1[1]); 
    		close(pipe2[0]);
			close(pipe2[1]);  
    		int sum=0,buff;
    		for(;;)
    		{
    			if(read(pipe1[0],&buff,sizeof(int))==0)
				{										
					int fd= creat(argv[2],0777);
				    fd=open(argv[2],O_WRONLY);
				    write(fd,&sum,sizeof(int));
				    printf("la somma dei caratteri è:%d \n",sum);
				    close(pipe1[0]);				    
					exit(EXIT_SUCCESS);
				}
    			sum = sum +buff;

    		}
    	}
    	else                     //figlio2
    	{
    		close(pipe1[0]);
    		close(pipe1[1]);
    		close(pipe2[0]);
    		int nread, read_buf[2];
    		fd_in = open(argv[1], O_RDONLY);
    		if ( fd_in < 0 ){
    		            perror("P2: errore nell'apertura del file di input");
    		            close(fd_in);
    		            kill(pid1, SIGALRM);
    		            exit(EXIT_FAILURE);
    		        }
    		while ( (nread = read(fd_in, read_buf, 2*sizeof(int))) > 0 ) /*Fino a EOF*/{
    		        int seek_val;
    		        if(read_buf[0] >= read_buf[1]) /* A >= B */{
    		        	write(pipe2[1],&read_buf[0],sizeof(int));
    		        } else{
    		        	write(pipe2[1],&read_buf[1],sizeof(int));
    		        }

    		        seek_val = lseek(fd_in, sizeof(int), SEEK_CUR);
				    if ( seek_val < 0 ){
					   perror("P2: errore nel puntatore del file di input");
					   close(fd_in);
					   exit(EXIT_FAILURE);
				    }
    		}
			close(fd_in);
			close(pipe2[1]);
			exit(EXIT_SUCCESS);
    	}
    }
    else {                         //figlio1
    	close(pipe1[0]);
    	close(pipe2[1]);
    	int seek_val,buff1,buff2;
    	fd_in = open(argv[1], O_RDWR);
    	for(;;)
    	{
			seek_val = lseek(fd_in, 2*sizeof(int), SEEK_CUR);
					if ( seek_val < 0 ){
					   perror("P1: errore indice del file di input");
					   close(fd_in);
					   exit(EXIT_FAILURE);
					}
			read(fd_in,&buff1, sizeof(int));
			if(read(pipe2[0],&buff2,sizeof(int))==0)
			{
				close(pipe2[0]);
				close(pipe1[1]);
				close(fd_in);
				exit(EXIT_SUCCESS);
			}
			if(buff1!=buff2)
			{
				seek_val = lseek(fd_in, -sizeof(int), SEEK_CUR);
				if ( seek_val < 0 ){
				   perror("P1: errore indice del file di input");
				   close(fd_in);
				   exit(EXIT_FAILURE);
				}
				write(fd_in,&buff2,sizeof(int));
				write(pipe1[1],&buff2,sizeof(int));
			}
    	}
    }
}
