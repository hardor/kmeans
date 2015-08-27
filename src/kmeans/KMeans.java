// Decompiled by DJ v3.12.12.100 Copyright 2015 Atanas Neshkov  Date: 28.08.2015 1:48:47
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   KMeans.java

package kmeans;

import java.util.Random;

public class KMeans
{

    public KMeans()
    {
    }

    double[][] setCentroides(int nClusteres, int nDimensoes, double data[][])
    {
        double centroides[][] = new double[nClusteres][nDimensoes];
        Random random = new Random();
        System.out.println("Init Centroids");
        for(int i = 0; i < centroides.length; i++)
        {
            int pos = random.nextInt(data.length);
            System.out.print((new StringBuilder()).append("Cluster ").append(i + 1).append(": ").toString());
            for(int j = 0; j < centroides[i].length; j++)
            {
                centroides[i][j] = data[pos][j];
                System.out.print((new StringBuilder()).append(centroides[i][j]).append(" ").toString());
            }

            System.out.println();
        }

        return centroides;
    }

    int[] allocateObjects(double data[][], double centroides[][], boolean flagDistance)
    {
        int location[] = new int[data.length];
        for(int i = 0; i < data.length; i++)
        {
            double distMin = 1.7976931348623157E+308D;
            for(int c = 0; c < centroides.length; c++)
            {
                double distCurrent;
                if(flagDistance)
                    distCurrent = evklidDist(centroides[c], data[i]);
                else
                    distCurrent = manhDist(centroides[c], data[i]);
                if(distCurrent < distMin)
                {
                    distMin = distCurrent;
                    location[i] = c;
                }
            }

        }

        return location;
    }

    double[][] changeCentroids(double centroids[][], int location[], double data[][])
    {
        double newCentroids[][] = new double[centroids.length][centroids[0].length];
        for(int indexCentroids = 0; indexCentroids < centroids.length; indexCentroids++)
        {
            double newCentroid[] = new double[centroids[0].length];
            int num = 0;
            for(int k = 0; k < location.length; k++)
                if(verify(location[k], indexCentroids))
                {
                    num++;
                    summCentroide(newCentroid, data[k]);
                }

            newCentroids[indexCentroids] = averageCentroid(newCentroid, num);
        }

        return newCentroids;
    }

    boolean verify(int location, int indexCentroids)
    {
        return location == indexCentroids;
    }

    double[] summCentroide(double newCentroid[], double vector[])
    {
        for(int i = 0; i < newCentroid.length; i++)
            newCentroid[i] += vector[i];

        return newCentroid;
    }

    double evklidDist(double x[], double y[])
    {
        double summ = 0.0D;
        for(int i = 0; i < x.length; i++)
            summ += Math.pow(x[i] - y[i], 2D);

        return Math.sqrt(summ);
    }

    double manhDist(double x[], double y[])
    {
        double summ = 0.0D;
        for(int i = 0; i < x.length; i++)
            summ += Math.abs(x[i] - y[i]);

        return summ;
    }

    double[] averageCentroid(double newCentroid[], int num)
    {
        for(int i = 0; i < newCentroid.length; i++)
            newCentroid[i] = newCentroid[i] / (double)num;

        return newCentroid;
    }

    boolean isChanged(int oldLocation[], int currLocation[])
    {
        for(int i = 0; i < oldLocation.length; i++)
            if(oldLocation[i] != currLocation[i])
                return true;

        return false;
    }

    double errorQuadratic(double data[][], double centroides[][], int location[])
    {
        double error = 0.0D;
        for(int i = 0; i < centroides.length; i++)
        {
            for(int k = 0; k < location.length; k++)
                if(verify(location[k], i))
                    error += Math.pow(evklidDist(centroides[i], data[k]), 2D);

        }

        return error;
    }
}
