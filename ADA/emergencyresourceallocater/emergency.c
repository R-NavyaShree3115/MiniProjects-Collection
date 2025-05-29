
#include <stdio.h>
#include <limits.h>
#include <string.h>

#define MAX 10

typedef struct {
    char id[10];
    int urgency;
    int deadline;
    int resourceNeed;
    int location;
} Site;

int n;
int dist[MAX][MAX];
Site sites[MAX];
int visited[MAX];
int bestPath[MAX];
int currentPath[MAX];
int maxScore = INT_MIN;
int bestArrivalTimes[MAX+1];

int calculateScore(int path[], int *lateCount, int arrivalTimes[]) {
    int time = 0, score = 0;
    *lateCount = 0;

    for (int i = 0; i < n - 1; i++) {
        time += dist[path[i]][path[i + 1]];
        arrivalTimes[i + 1] = time;
        int loc = path[i + 1];
        int urgency = sites[loc].urgency;
        int deadline = sites[loc].deadline;
        if (time <= deadline) {
            score += 10 * urgency;
        } else {
            score += 3 * urgency;
            (*lateCount)++;
        }
    }

    return score;
}

void tsp(int level) {
    if (level == n) {
        currentPath[level] = 0;
        int lateCount = 0;
        int arrivalTimes[MAX + 1] = {0};
        int score = calculateScore(currentPath, &lateCount, arrivalTimes);

        if (score > maxScore) {
            maxScore = score;
            memcpy(bestPath, currentPath, sizeof(int) * (n + 1));
            memcpy(bestArrivalTimes, arrivalTimes, sizeof(int) * (n + 1));
        }
        return;
    }

    for (int i = 1; i < n; i++) {
        if (!visited[i]) {
            visited[i] = 1;
            currentPath[level] = i;
            tsp(level + 1);
            visited[i] = 0;
        }
    }
}

void printColor(const char* text, const char* color) {
    printf("%s%s\x1b[0m", color, text);
}

void printSummary() {
    printf("\n=== Emergency Response Summary ===\n");
    int onTime = 0, late = 0, totalUrgency = 0, totalResources = 0;

    for (int i = 1; i < n; i++) {
        int loc = bestPath[i];
        totalUrgency += sites[loc].urgency;
        totalResources += sites[loc].resourceNeed;
    }

    for (int i = 1; i < n; i++) {
        int loc = bestPath[i];
        int arrival = bestArrivalTimes[i];
        int deadline = sites[loc].deadline;
        printf("%d. %s (Location %d) - Arrival: %d min, Deadline: %d - ",
               i, sites[loc].id, loc, arrival, deadline);
        if (arrival <= deadline) {
            printColor("On-Time\n", "\x1b[32m");
            onTime++;
        } else {
            printColor("Late\n", "\x1b[31m");
            late++;
        }
    }

    printf("\nTotal Sites: %d\n", n - 1);
    printf("On-Time: %d, Late: %d\n", onTime, late);
    printf("Urgency Total: %d, Resource Needs Total: %d\n", totalUrgency, totalResources);
    printf("Final Performance Score: %d\n", maxScore);
    printf("==================================\n");
}

int main() {
    printf("Enter number of sites (including base, max %d): ", MAX);
    if (scanf("%d", &n) != 1 || n < 2 || n > MAX) {
        printf("Invalid number of sites.\n");
        return 1;
    }

    printf("Enter %dx%d distance matrix:\n", n, n);
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++) {
            if (scanf("%d", &dist[i][j]) != 1 || dist[i][j] < 0) {
                printf("Invalid distance.\n");
                return 1;
            }
        }

    printf("Enter details for each site (ID Urgency Deadline ResourceNeed):\n");
    for (int i = 1; i < n; i++) {
        if (scanf("%s %d %d %d", sites[i].id, &sites[i].urgency, &sites[i].deadline, &sites[i].resourceNeed) != 4) {
            printf("Invalid input.\n");
            return 1;
        }
        sites[i].location = i;
    }

    strcpy(sites[0].id, "Base");
    sites[0].urgency = 0;
    sites[0].deadline = INT_MAX;
    sites[0].resourceNeed = 0;
    sites[0].location = 0;

    memset(visited, 0, sizeof(visited));
    visited[0] = 1;
    currentPath[0] = 0;

    printf("\nCalculating best emergency route...\n");
    tsp(1);

    printf("\nBest Emergency Route:\n");
    for (int i = 0; i < n; i++) {
        int loc = bestPath[i];
        printf("%d. %s (Location %d) - Arrival: %d min\n", i + 1, sites[loc].id, loc, bestArrivalTimes[i]);
    }
    printf("%d. %s (Location 0) - Return\n", n + 1, sites[0].id);
    printf("Total Time: %d mins\n", bestArrivalTimes[n - 1] + dist[bestPath[n - 1]][0]);

    printSummary();
    return 0;
}
