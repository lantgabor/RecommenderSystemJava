package com.lantg;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int review_n, user_n, book_n;
        review_n = sc.nextInt();
        user_n = sc.nextInt();
        book_n = sc.nextInt();

        double[][] R = Matrix.empty(user_n, book_n);

        for(int i = 0; i < review_n; i++){
            R[sc.nextInt()][sc.nextInt()] = sc.nextInt();
        }

        int N = R.length;
        int M = R[0].length;
        int K = 4;

        double[][] P = Matrix.random(N,K);
        double[][] Q = Matrix.random(M,K);

        int s = 1750;
        double a = 0.0002;
        double b = 0.02;

        matrix_factor(R, P, Q, K, s, a, b);

        double[][] nR = Matrix.multiply(P, Matrix.transpose(Q));

        int topN = 10;

        for(int j = 0; j < nR.length; j++) {
            int n = 0;
            int[] tmp = ArrayUtils.argsort(nR[j], false);
            for (int i = 0; i < tmp.length; i++) {
                if(R[j][tmp[i]] == 0) {
                    if(n < topN) {
                        System.out.print(tmp[i]);
                        if (n < topN -1) System.out.print('\t');
                        n++;
                    }
                }
            }
            System.out.print('\n');
        }
    }


    public static void matrix_factor(double[][] R, double[][] P, double[][] Q, int K, int steps, double alpha, double beta){
        for(int step = 0; step < steps; step++){
            for(int i = 0; i < R.length; i++){
                for(int j = 0; j < R[i].length; j++){
                    if(R[i][j] > 0 ){
                        double eij = R[i][j] - Matrix.dot(P[i], Q[j]);
                        for( int k = 0; k < K; k++){
                            P[i][k] = P[i][k] + alpha * (2 * eij * Q[j][k] - beta * P[i][k]);
                            Q[j][k] = Q[j][k] + alpha * (2 * eij * P[i][k] - beta * Q[j][k]);
                        }
                    }
                }
            }
            double[][] eR = Matrix.multiply(P,Matrix.transpose(Q));
            double e = 0;
            for(int i = 0; i < R.length; i++){
                for(int j = 0; j < R[i].length; j++){
                    if(R[i][j] > 0){
                        e = e + Math.pow( R[i][j] - Matrix.dot(P[i], Q[j]), 2);
                        for(int k = 0; k < K; k++){
                            e = e + (beta/2) * ( Math.pow(P[i][k], 2) + Math.pow(Q[j][k],2) );
                        }
                    }
                }
            }
            if(e < 0.001) break;
        }
    }
}
/*
Teszt adatok
13 5 4
0 0 5
0 1 3
0 3 1
1 0 4
1 3 1
2 0 1
2 1 1
2 3 5
3 0 1
3 3 4
4 1 1
4 2 5
4 3 4
*/