import java.io.*;
import java.util.*;

public class StorageCaseStudy {

    public static void blockCreationSimulation() throws IOException {

        // Inputs
        int largeHospitalVisitsLB = 200;
        int largeHospitalVisitsUB = 500;
        int smallFacilityVisitsLB = 50;
        int smallFacilityVisitsUB = 200;
        double ImageProb = 0.05;
        int imageCountLB = 1;
        int imageCountUB = 5;
        double imageSizeLB = 1, imgSizeDgrL = 1;
        double imageSizeUB = 3, imgSizeDgrU = 3;
        // double videoProb = 0.1;
        // int videoCountLB = 1;
        // int videoCountUB = 4;
        // double videoSizeLB = 10;
        // double videoSizeUB = 50;
        double textSizeLB = 0.003, txtSizeDgrL = 0.003;
        double textSizeUB = 0.007, txtSizeDgrU = 0.007;
        double growthRate = 0.05/5;
        int transactionsPerBlock = 500;
        int largeHospitalsInNetwork = 10;
        int smallFacilitiesInNetwork = 30;
        int daysPerYear = 365;
        int years = 50;

        // Used for diagnoses
        long txIDseed = 0;
        long blockIDseed = 0;
        double sizePerYear = 0.0;
        double prevSizePerYear = 0.0;
        double percentIncrease = 0.0;
        double totalSize = 0.0;
        double imageCount = 0.0;
        double imageSizeTotal = 0.0;
        double imageYes = 0.0;
        double totalVisits = 0.0;

        // Output setup
        // output.txt contains the size of each block
        // output1.txt cointains the year, data generated in that year, and total data generated thus far
        FileWriter fw = new FileWriter("output.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        FileWriter fw1 = new FileWriter("output1.txt");
        BufferedWriter bw1 = new BufferedWriter(fw1);

        SimpleBlock block = new SimpleBlock();

        // I'll set these up once beforehand so hopefully memory won't be wasted
        // Randomly generate how many small facility visits (per facility) per day
        ArrayList<Integer> smallFacilityVisits = new ArrayList<>();
        for (int sf = 0; sf < smallFacilitiesInNetwork; sf++) {
            smallFacilityVisits.add(getRandom(smallFacilityVisitsLB, smallFacilityVisitsUB));
        }
        // Randomly generate how many large hospital visits (per hospital) per day
        ArrayList<Integer> largeHospitalVisits = new ArrayList<>();
        for (int sf = 0; sf < largeHospitalsInNetwork; sf++) {
            largeHospitalVisits.add(getRandom(largeHospitalVisitsLB, largeHospitalVisitsUB));
        }

        // For each year
        for (int y = 0; y < years; y++) {

            // Each year, the file sizes should increase a set amount. Start with 10%, then
            // 20%,... so on
            if (y > 0) {
                if (y % 5 == 0){
                    imgSizeDgrL = imageSizeLB;
                    imgSizeDgrU = imageSizeUB;
                    txtSizeDgrL = textSizeLB;
                    textSizeUB = txtSizeDgrU;
                }
                imageSizeLB += imgSizeDgrL * growthRate;
                imageSizeUB += imgSizeDgrU * growthRate;
                // videoSizeLB *= 1.1;
                // videoSizeUB *= 1.1;
                textSizeLB += txtSizeDgrL * growthRate;
                textSizeUB += txtSizeDgrU * growthRate;
            }

            // For each day
            for (int d = 0; d < daysPerYear; d++) {

                // Randomly generate how many small facility visits (per facility) per day
                for (int sf = 0; sf < smallFacilitiesInNetwork; sf++) {
                    smallFacilityVisits.set(sf, getRandom(smallFacilityVisitsLB, smallFacilityVisitsUB));
                }
                // Randomly generate how many large hospital visits (per hospital) per day
                for (int lh = 0; lh < largeHospitalsInNetwork; lh++) {
                    largeHospitalVisits.set(lh, getRandom(largeHospitalVisitsLB, largeHospitalVisitsUB));
                }

                // Although the transactions would never be generated in this order, the order
                // should have no affect on the output blocks
                // Generate the transactions for each small facility
                for (int visits : smallFacilityVisits) {
                    totalVisits += visits;
                    for (int visit = 0; visit < visits; visit++) {
                        double totalVisitSize = 0.0;

                        // // Randomly check if video file(s) generated
                        // if (Math.random() <= videoProb){
                        // // Randomly choose number of video files
                        // int videoFiles = getRandom(videoCountLB, videoCountUB);
                        // for (int vfs=0; vfs<videoFiles; vfs++){
                        // // Randomly choose size of each video file
                        // totalVisitSize = getRandom(videoSizeLB, videoSizeUB);
                        // }
                        // }

                        // Randomly check if image file(s) generated
                        if (Math.random() <= ImageProb) {
                            imageYes++;
                            // Randomly choose number of image files
                            int imageFiles = getRandom(imageCountLB, imageCountUB);
                            imageCount += imageFiles;
                            for (int ifs = 0; ifs < imageFiles; ifs++) {
                                // Randomly choose size of each video file
                                double imageSize = getRandom(imageSizeLB, imageSizeUB);
                                totalVisitSize += imageSize;
                                imageSizeTotal += imageSize;
                            }
                        }

                        // Add text
                        totalVisitSize += getRandom(textSizeLB, textSizeUB);

                        // Check if block is full
                        if (block.txs.size() == transactionsPerBlock) {
                            bw.write(block.blockSize + "\n");
                            block = new SimpleBlock(++blockIDseed, new SimpleTransaction(txIDseed, totalVisitSize));
                        } else {
                            block.add(new SimpleTransaction(txIDseed, totalVisitSize));
                        }

                        txIDseed++;
                        sizePerYear += totalVisitSize;
                        totalSize += totalVisitSize;
                    }
                }

                // Generate the transactions for each large hospital
                for (int visits : largeHospitalVisits) {
                    totalVisits += visits;
                    for (int visit = 0; visit < visits; visit++) {
                        double totalVisitSize = 0;

                        // // Randomly check if video file(s) generated
                        // if (Math.random() <= videoProb){
                        // // Randomly choose number of video files
                        // int videoFiles = getRandom(videoCountLB, videoCountUB);
                        // for (int vfs=0; vfs<videoFiles; vfs++){
                        // // Randomly choose size of each video file
                        // totalVisitSize = getRandom(videoSizeLB, videoSizeUB);
                        // }
                        // }

                        // Randomly check if image file(s) generated
                        if (Math.random() <= ImageProb) {
                            imageYes++;
                            // Randomly choose number of image files
                            int imageFiles = getRandom(imageCountLB, imageCountUB);
                            imageCount += imageFiles;
                            for (int ifs = 0; ifs < imageFiles; ifs++) {
                                // Randomly choose size of each video file
                                double imageSize = getRandom(imageSizeLB, imageSizeUB);
                                totalVisitSize += imageSize;
                                imageSizeTotal += imageSize;
                            }
                        }

                        // Add text
                        totalVisitSize += getRandom(textSizeLB, textSizeUB);

                        // Check if block is full
                        if (block.txs.size() == transactionsPerBlock) {
                            block = new SimpleBlock(++blockIDseed, new SimpleTransaction(txIDseed, totalVisitSize));
                        } else {
                            block.add(new SimpleTransaction(txIDseed, totalVisitSize));
                        }

                        txIDseed++;
                        sizePerYear += totalVisitSize;
                        totalSize += totalVisitSize;
                    }
                }
            }

            int year = y + 1;
            System.out.println("Year " + year + ":");

            // Check average visits per day
            double avgVisitsPerDay = totalVisits / 365;
            System.out.println("   Average visits per day: " + String.format("%.0f", avgVisitsPerDay));

            // Check percentage image visits
            double percentImageVisits = imageYes / totalVisits * 100;
            System.out.println("   Average image visits percent: " + String.format("%.2f", percentImageVisits) + "%");
            totalVisits = 0;

            // Check average images per imageCount
            double avgImagesPer = imageCount / imageYes;
            System.out.println("   Average images created: " + String.format("%.3f", avgImagesPer));

            // Check average size of each image
            double avgImageSize = imageSizeTotal / imageCount;
            System.out.println("   Average image size: " + String.format("%.3f", avgImageSize));
            imageYes = 0;
            imageCount = 0;
            imageSizeTotal = 0;

            // Print out yearly size and % increase
            sizePerYear = ((double) sizePerYear / 1000000.0);
            if (prevSizePerYear != 0) {
                percentIncrease = ((sizePerYear - prevSizePerYear) / prevSizePerYear) * 100;
            }
            System.out.println("   Size: " + String.format("%.3f", sizePerYear) + " TB\n   Increase: "
                    + String.format("%.2f", percentIncrease) + "%");
            double temp = totalSize / 1000000.0;
            bw1.write(year + "\t" + sizePerYear + "\t" + temp + "\n");
            prevSizePerYear = sizePerYear;
            sizePerYear = 0;

        }

        // Convert to TB
        totalSize /= 1000000;
        System.out.println("Total size: " + totalSize);

        bw.close();
        fw.close();
        bw1.close();
        fw1.close();
    }

    public static int getRandom(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static double getRandom(double min, double max) {
        return (Math.random() * (max - min) + min);
    }

}