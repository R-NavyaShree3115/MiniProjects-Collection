#include <stdio.h>
#include <limits.h>
#include <string.h>

#define MAX 10

typedef struct {
    char id[10];
    int priority;
    int deadline;
    int location;
} Package;

int n;
int dist[MAX][MAX];
Package packages[MAX];
int visited[MAX];
int bestPath[MAX];
int currentPath[MAX];
int maxScore = INT_MIN;
int bestArrivalTimes[MAX+1];

int calculateScore(int path[], int *lateDeliveries, int arrivalTimes[]) {
    int time = 0;
    int score = 0;
    *lateDeliveries = 0;
    for (int i = 0; i < n - 1; i++) {
        time += dist[path[i]][path[i + 1]];
        arrivalTimes[i + 1] = time;
        int loc = path[i + 1];
        int priority = packages[loc].priority;
        int deadline = packages[loc].deadline;

        if (time <= deadline)
            score += 10 * priority;
        else {
            score += 2 * priority;
            (*lateDeliveries)++;
        }
    }
    return score;
}

void tsp(int level, int currCost) {
    if (level == n) {
        currentPath[level] = 0;
        int lateDeliveries = 0;
        int arrivalTimes[MAX+1] = {0};
        int score = calculateScore(currentPath, &lateDeliveries, arrivalTimes);

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
            tsp(level + 1, currCost + dist[currentPath[level - 1]][i]);
            visited[i] = 0;
        }
    }
}

void printColored(const char* text, const char* colorCode) {
    printf("%s%s\x1b[0m", colorCode, text);
}

void printRouteSummary() {
    printf("\n=== Delivery Route Summary ===\n");
    int onTimeCount = 0, lateCount = 0;
    int totalPriority = 0;
    int weightedOnTimeScore = 0;
    int weightedLateScore = 0;

    for (int i = 1; i < n; i++) {
        int loc = bestPath[i];
        totalPriority += packages[loc].priority;
    }

    for (int i = 1; i < n; i++) {
        int loc = bestPath[i];
        int arrival = bestArrivalTimes[i];
        int deadline = packages[loc].deadline;
        int priority = packages[loc].priority;

        printf("%d. %s (Location %d) - Arrival Time: %d mins, Deadline: %d mins - ",
            i, packages[loc].id, loc, arrival, deadline);

        if (arrival <= deadline) {
            printColored("On-Time\n", "\x1b[32m");
            onTimeCount++;
            weightedOnTimeScore += 10 * priority;
        } else {
            printColored("Late\n", "\x1b[31m");
            lateCount++;
            weightedLateScore += 2 * priority;
        }
    }
    int totalDeliveries = n - 1;
    double onTimePercent = totalDeliveries == 0 ? 0 : (onTimeCount * 100.0) / totalDeliveries;
    double latePercent = totalDeliveries == 0 ? 0 : (lateCount * 100.0) / totalDeliveries;

    printf("\nTotal Deliveries: %d\n", totalDeliveries);
    printf("On-Time Deliveries: %d (%.2f%%)\n", onTimeCount, onTimePercent);
    printf("Late Deliveries: %d (%.2f%%)\n", lateCount, latePercent);
    printf("Total Priority Sum: %d\n", totalPriority);
    printf("Weighted On-Time Score Contribution: %d\n", weightedOnTimeScore);
    printf("Weighted Late Score Contribution: %d\n", weightedLateScore);
    printf("Total Performance Score (On-Time*10 + Late*2): %d\n", maxScore);
    printf("==============================\n");
}

int main() {
    printf("Enter number of delivery locations (including base, max %d): ", MAX);
    if (scanf("%d", &n) != 1 || n < 2 || n > MAX) {
        printf("Invalid number of locations. Please enter a number between 2 and %d.\n", MAX);
        return 1;
    }

    printf("Enter distance matrix (%dx%d) - distances must be non-negative:\n", n, n);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            if (scanf("%d", &dist[i][j]) != 1 || dist[i][j] < 0) {
                printf("Invalid input: distance must be a non-negative integer.\n");
                return 1;
            }
        }
    }

    printf("Enter package details for each delivery location (ID Priority Deadline_in_minutes):\n");
    printf("(Starting from location 1 to %d, Base is location 0 and no package details needed)\n", n-1);
    for (int i = 1; i < n; i++) {
        if (scanf("%9s %d %d", packages[i].id, &packages[i].priority, &packages[i].deadline) != 3) {
            printf("Invalid input for package details.\n");
            return 1;
        }
        if (packages[i].priority <= 0 || packages[i].deadline <= 0) {
            printf("Priority and deadline must be positive integers.\n");
            return 1;
        }
        packages[i].location = i;
    }

    strcpy(packages[0].id, "Base");
    packages[0].priority = 0;
    packages[0].deadline = INT_MAX;
    packages[0].location = 0;

    memset(visited, 0, sizeof(visited));
    visited[0] = 1;
    currentPath[0] = 0;

    printf("\nCalculating optimal delivery route... (Note: This may take time for larger n)\n");
    tsp(1, 0);

    printf("\nOptimal Delivery Route and Timing:\n");
    int time = 0;
    for (int i = 0; i < n; i++) {
        int loc = bestPath[i];
        printf("%d. %s (Location %d) - Arrival Time: %d mins\n",
            i + 1, packages[loc].id, loc, bestArrivalTimes[i]);
        if (i < n - 1)
            time += dist[bestPath[i]][bestPath[i + 1]];
    }
    printf("%d. %s (Location %d) - Return to Base\n", n + 1, packages[0].id, 0);
    time += dist[bestPath[n - 1]][0];
    printf("Total Estimated Route Time: %d minutes\n", time);

    printRouteSummary();

    return 0;
}
