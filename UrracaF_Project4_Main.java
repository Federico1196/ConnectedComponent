import java.io.*;
import java.util.Scanner;

public class UrracaF_Project4_Main {
    
    public static void main(String[] args) throws IOException {
        Scanner inFile = new Scanner(new File(args[0]));
        BufferedWriter RFprettyPrintFile = new BufferedWriter(new FileWriter(args[2]));
        BufferedWriter labelFile = new BufferedWriter(new FileWriter(args[3]));
        BufferedWriter propertyFile = new BufferedWriter(new FileWriter(args[4]));
        FileWriter debugFile = new FileWriter(args[5]);

        int numRows = inFile.nextInt();
        int numCols = inFile.nextInt();
        int minVal = inFile.nextInt();
        int maxVal = inFile.nextInt();

        CCLabel CC = new CCLabel(numRows, numCols, minVal, maxVal);

        // step 1
        CC.zero2D(CC.zeroFramedAry);

        // step 2
        CC.loadImage(inFile);

        // step 3
        int whichConnectness = Integer.parseInt(args[1]);

        // step 4
        if (whichConnectness == 4) {
            CC.zeroFramedAry = CC.connect4Pass1(CC.zeroFramedAry, debugFile);
            RFprettyPrintFile.write("Result of Pass 1: \n");
            CC.imgReformat(CC.zeroFramedAry, RFprettyPrintFile);
            RFprettyPrintFile.write("\n");
            RFprettyPrintFile.write("EQ Ary after Pass 1: \n");
            CC.printEQAry(RFprettyPrintFile);
            RFprettyPrintFile.write("\n");
            CC.zeroFramedAry = CC.connect4Pass2(CC.zeroFramedAry, debugFile);
            RFprettyPrintFile.write("\n");
            RFprettyPrintFile.write("Result of Pass 2: \n");
            CC.imgReformat(CC.zeroFramedAry, RFprettyPrintFile);
            RFprettyPrintFile.write("\n");
            RFprettyPrintFile.write("EQ Ary after Pass 2: \n");
            CC.printEQAry(RFprettyPrintFile);
            RFprettyPrintFile.write("\n");
        }

        // step 5
        if (whichConnectness == 8) {
            CC.zeroFramedAry = CC.connect8Pass1(CC.zeroFramedAry, debugFile);
            RFprettyPrintFile.write("\n");
            RFprettyPrintFile.write("Result of Pass 1: \n");
            CC.imgReformat(CC.zeroFramedAry, RFprettyPrintFile);
            RFprettyPrintFile.write("\n");
            RFprettyPrintFile.write("EQ Ary after Pass 1: \n");
            CC.printEQAry(RFprettyPrintFile);
            RFprettyPrintFile.write("\n");
            CC.zeroFramedAry = CC.connect8Pass2(CC.zeroFramedAry, debugFile);
            RFprettyPrintFile.write("Result of Pass 2: \n");
            CC.imgReformat(CC.zeroFramedAry, RFprettyPrintFile);
            RFprettyPrintFile.write("EQ Ary after Pass 2:\n");
            CC.printEQAry(RFprettyPrintFile);
            RFprettyPrintFile.write("\n");
        }
        
        // step 6
        CC.setTrueNum(CC.manageEQAry());
        RFprettyPrintFile.write("\n");
        RFprettyPrintFile.write("EQ Ary after management: \n");
        CC.printEQAry(RFprettyPrintFile);
        RFprettyPrintFile.write("\n");
        CC.setNewMin(0);
        CC.setNewMax(CC.getTrueNum());
        CC.allocateCCproperty(CC.getTrueNum());

        // step 7
        CC.connectPass3(CC.zeroFramedAry, CC.CCproperty, debugFile);

        // step 8
        RFprettyPrintFile.write("Result of Pass 3 \n");
        CC.imgReformat(CC.zeroFramedAry, RFprettyPrintFile);

        // step 9
        RFprettyPrintFile.write("EQ Ary after Pass 3 \n");
        CC.printEQAry(RFprettyPrintFile);
        RFprettyPrintFile.write("\n");

        // step 10
        labelFile.write(Integer.toString(CC.getNumRows()));
        labelFile.write(" ");
        labelFile.write(Integer.toString(CC.getNumCols()));
        labelFile.write(" ");
        labelFile.write(Integer.toString(CC.getNewMin()));
        labelFile.write(" ");
        labelFile.write(Integer.toString(CC.getNewMax()));
        labelFile.write("\n");

        // step 11
        CC.printImg(labelFile);

        // step 12
        CC.printCCproperty(propertyFile);

        // step 13
        CC.drawBoxes(CC.zeroFramedAry, CC.CCproperty, debugFile);

        // step 14
        RFprettyPrintFile.write("After boxes:\n");
        CC.imgReformat(CC.zeroFramedAry, RFprettyPrintFile);

        // step 15
        RFprettyPrintFile.write("True number of Connected Components: ");
        RFprettyPrintFile.write(Integer.toString(CC.getTrueNum()));

        // step 16
        inFile.close();
        labelFile.close();
        RFprettyPrintFile.close();
        propertyFile.close();
        debugFile.close();
    }
}