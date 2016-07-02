#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <signal.h>
#include <sys/wait.h>

void print_usage(char* prog_name);
void wait_child();
void alrm_handle(int signum);
char* read_line(int file);

int pid1;

int main(int argc, char *argv[]) {
    
    int timeout, fileout;
    char *line;

    //Controls
    if (argc != 4) {
       print_usage(argv[0]);
       exit(EXIT_FAILURE);
    }

    timeout = atoi(argv[2]);

    if (timeout <= 0) {
        printf("Timeout deve essere un intero positivo\n");
        print_usage(argv[0]);
        exit(EXIT_FAILURE);
    }

    if (strlen(argv[3]) != 1 || (argv[3][0] != 'd' && argv[3][0] != 'a')) {
        printf("tipo_op deve o il carattere 'd' o il carattere 'a'\n");
        print_usage(argv[0]);
        exit(EXIT_FAILURE);
    }
    
    if ((fileout = open(argv[1], O_RDWR, 0755)) < 0) {
        printf("Errore in apertura del fileout\n");
        exit(EXIT_FAILURE);
    }

    pid1 = fork();

    if (pid1 == 0) {            //CHILD
        printf("[%d]\n", getpid());
        close(1);
        dup(fileout);

        execlp("java", "java", "-jar", "Aeroporto.jar", (char *)0);

        exit(EXIT_SUCCESS); 
    }

    else if (pid1 > 0) {            //FATHER
        printf("[%d]: Created child with pid %d\n",getpid(), pid1);
        signal(SIGALRM, &alrm_handle);
        alarm(timeout);

        printf("Aspetto il processo P1\n");
        wait_child();

        lseek(fileout, 0, SEEK_SET);
        
        do {
            line = read_line(fileout);
            if (argv[3][0] == 'a') {
                if (strstr(line, "atterraggio") != NULL)
                    printf("%s", line);
            }
            else if (argv[3][0] == 'd') {
                if (strstr(line, "decollo") != NULL)
                    printf("%s", line);
            }
        } while (strlen(line) > 0);

    }

    else {
        perror("Fork error");
        exit(EXIT_FAILURE);
    }

    return 0;
}

void alrm_handle(int signum) {
    printf("TIMEOUT!\n");
    kill(pid1, SIGKILL);
}

char* read_line(int file) {

    int i = 0;
    char ch;
    char* result;

    while((read(file, &ch, sizeof(char))) > 0) {
        i++;
        if (ch == '\n') 
            break;
    }
    
    result = malloc(sizeof(char) * i);

    lseek(file, -i * sizeof(char), SEEK_CUR);

    i = 0;

    while((read(file, &ch, sizeof(char))) > 0) {
        result[i] = ch;
        i++;
        if (ch == '\n')
            break;
    }
    result[i] = '\0';

    return result;
}

void wait_child() {
    int status, pid;
    pid = wait(&status);
    if(pid < 0) {
        perror("Error while waiting for a child to terminate");
        exit(EXIT_FAILURE);
    }
    if (WIFEXITED(status)) {
        printf("[%d]: Terminated with exit status %d\n", pid, WEXITSTATUS(status));
    }
    else if (WIFSIGNALED(status)) {
        printf("[%d]: Terminated by signal no %d\n", pid, WTERMSIG(status));
    }
}

void print_usage(char* prog_name) {
    printf("Usage: %s fileout timeout tipo_op\n", prog_name);
}
