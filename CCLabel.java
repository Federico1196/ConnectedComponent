import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CCLabel extends Property{
    private int numRows, numCols, minVal, maxVal, newLabel, newMin, newMax, trueNumCC;
    public int zeroFramedAry[][];
    private int nonZeroNeighborAry[];
    private int EQAry[];
    Property[] CCproperty;

    CCLabel(int numRows, int numCols, int minVal, int maxVal) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.newLabel = 0;
        this.zeroFramedAry = new int[numRows + 2][numCols + 2];
        this.nonZeroNeighborAry = new int[5];
        this.EQAry = new int[(numRows * numCols) / 4];
    }

    public int[][] zero2D(int[][] arr) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                arr[i][j] = 0;
            }
        }
        return arr;
    }

    public int[] minus1D(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = -1;
        }
        return arr;
    }

    public void loadImage(Scanner b) throws IOException {
        for (int i = 1; i <= numRows; i++) {
            for (int j = 1; j <= numCols; j++) {
                this.zeroFramedAry[i][j] = b.nextInt();
            }
        }
    }

    public void imgReformat(int[][] img, BufferedWriter prettyprint) throws IOException {
        String str = Integer.toString(this.newLabel);
        int width = str.length();

        for (int r = 1; r <= this.numRows; r++) {
            for (int c = 1; c <= this.numCols; c++) {
                if(img[r][c] > 0){
                    prettyprint.write(Integer.toString(img[r][c]));
                }else{
                    prettyprint.write(".");
                }
                String str2 = Integer.toString(img[r][c]);
                int WW = str2.length();
                prettyprint.write(" ");
                while (WW < width) {
                    prettyprint.write(" ");
                    WW++;
                }
            }
            prettyprint.write("\n");
        }
    }

    public int[][] connect4Pass1(int[][] img, FileWriter deBugFile) throws IOException {
        deBugFile.write("Entering connect4Pass1\n");
        int pixel, label;
        int a, b;
        int helper = 0;
        boolean flag = true;
        for (int i = 1; i <= this.numRows; i++) {
            for (int j = 1; j <= this.numCols; j++) {
                pixel = img[i][j];
                if (pixel > 0) {
                    helper = 0;
                    a = img[i - 1][j];
                    b = img[i][j - 1];
                    // case 1
                    if (a == 0 && b == 0) {
                        this.newLabel++;
                        updateEQ(this.newLabel, this.newLabel);
                        img[i][j] = this.newLabel;
                    } else {
                        if (a != 0) {
                            this.nonZeroNeighborAry[helper++] = a;
                        }
                        if (b != 0) {
                            this.nonZeroNeighborAry[helper++] = b;
                            if (a != b && a != 0) {
                                flag = false;
                            }
                        }
                        // case 2
                        if (flag) {
                            img[i][j] = this.nonZeroNeighborAry[0];
                            this.nonZeroNeighborAry = minus1D(this.nonZeroNeighborAry);
                        }
                        // case 3
                        else {
                            label = findMin();
                            if (img[i - 1][j] != 0) {
                                updateEQ(img[i - 1][j], label);
                            }
                            if (img[i][j - 1] != 0) {
                                updateEQ(img[i][j - 1], label);
                            }
                            img[i][j] = label;
                            flag = true;
                        }
                    }
                }
            }
        }
        deBugFile.write("After connected4 pass1, newLabel = " + newLabel + "\n");
        deBugFile.write("Leaving connect4Pass1\n");
        return img;
    }

    public int[][] connect4Pass2(int[][] img, FileWriter deBugFile) throws IOException {
        deBugFile.write("Entering connect4Pass2\n");
        int pixel, lbl;
        int e, g;
        int helper = 0;
        boolean flag = true;
        for (int i = this.numRows-1; i >= 1; i--) {
            for (int j = this.numCols-1; j >= 1; j--) {
                pixel = img[i][j];
                if (pixel > 0) {
                    helper = 0;
                    this.nonZeroNeighborAry[helper++] = pixel;
                    e = img[i][j + 1];
                    g = img[i + 1][j];
                    if (e != 0) {
                        this.nonZeroNeighborAry[helper++] = e;
                        if (pixel != e) {
                            flag = false;
                        }
                    }
                    if (g != 0) {
                        this.nonZeroNeighborAry[helper++] = g;
                        if (pixel != g) {
                            flag = false;
                        }
                    }
                    // case 3
                    if (!flag) {
                        lbl = findMin();
                        img[i][j] = lbl;
                        if (img[i + 1][j] != 0) {
                            img[i + 1][j] = lbl;
                        }
                        if (img[i][j + 1] != 0) {
                            img[i][j + 1] = lbl;
                        }
                        updateEQ(pixel, lbl);
                        flag = true;
                    }
                }
            }
        }
        deBugFile.write("After connected4 pass2, newLabel = " + newLabel + "\n");
        deBugFile.write("Entering connect4Pass2\n");
        return img;

    }

    public int[][] connect8Pass1(int[][] img, FileWriter deBugFile) throws IOException {
        deBugFile.write("Entering connect8Pass1\n");
        int pixel, minLabel;
        int helper;
        int a, b, c, d;
        boolean flag = true;
        for (int i = 1; i <= this.numRows; i++) {
            for (int j = 1; j <= this.numCols; j++) {
                pixel = img[i][j];
                if (pixel > 0) {
                    helper = 0;
                    a = img[i - 1][j - 1];
                    b = img[i - 1][j];
                    c = img[i - 1][j + 1];
                    d = img[i][j - 1];
                    // case 1
                    if (a == 0 && b == 0 && c == 0 && d == 0) {
                        this.newLabel++;
                        updateEQ(this.newLabel, this.newLabel);
                        img[i][j] = this.newLabel;
                    } else {
                        if (a != 0) {
                            this.nonZeroNeighborAry[helper++] = a;
                        }
                        if (b != 0) {
                            this.nonZeroNeighborAry[helper++] = b;
                        }
                        if (c != 0) {
                            this.nonZeroNeighborAry[helper++] = c;
                        }
                        if (d != 0) {
                            this.nonZeroNeighborAry[helper++] = d;
                        }
                        
                        for (int e = 1; e < helper; e++) {
                            if (this.nonZeroNeighborAry[e] != this.nonZeroNeighborAry[e - 1]) {
                                flag = false;
                            }
                        }
                        
                        // case 2
                        if (flag) {
                            img[i][j] = this.nonZeroNeighborAry[0];
                            this.nonZeroNeighborAry = minus1D(this.nonZeroNeighborAry);
                        }
                        // case 3
                        else {
                            minLabel = findMin();
                            if (img[i - 1][j - 1] != 0) {
                                updateEQ(img[i - 1][j - 1], minLabel);
                            }
                            if (img[i - 1][j] != 0) {
                                updateEQ(img[i - 1][j], minLabel);
                            }
                            if (img[i - 1][j + 1] != 0) {
                                updateEQ(img[i - 1][j + 1], minLabel);
                            }
                            if (img[i][j - 1] != 0) {
                                updateEQ(img[i][j - 1], minLabel);
                            }
                            img[i][j] = minLabel;
                            updateEQ(pixel, minLabel);
                        }
                    }
                }
            }
        }
        deBugFile.write("After connected8 pass1, newLabel = " + newLabel + "\n");
        deBugFile.write("Leaving connect8Pass1\n");
        return img;
    }

    public int[][] connect8Pass2(int[][] img, FileWriter deBugFile) throws IOException {
        deBugFile.write("Entering connect8Pass2\n");
        int pixel, label;
        int e, f, g, h;
        int helper = 0;
        boolean flag = true;
        for (int i = this.numRows-1; i >= 1; i--) {
            for (int j = this.numCols-1; j >= 1; j--) {
                pixel = img[i][j];
                if (pixel > 0) {
                    helper = 0;
                    this.nonZeroNeighborAry[helper++] = pixel;
                    e = img[i][j + 1];
                    f = img[i + 1][j - 1];
                    g = img[i + 1][j];
                    h = img[i + 1][j + 1];
                    if (e != 0) {
                        this.nonZeroNeighborAry[helper++] = e;
                        if (pixel != e) {
                            flag = false;
                        }
                    }
                    if (f != 0) {
                        this.nonZeroNeighborAry[helper++] = f;
                        if (pixel != f) {
                            flag = false;
                        }
                    }
                    if (g != 0) {
                        this.nonZeroNeighborAry[helper++] = g;
                        if (pixel != g) {
                            flag = false;
                        }
                    }
                    if (h != 0) {
                        this.nonZeroNeighborAry[helper++] = h;
                        if (pixel != h) {
                            flag = false;
                        }
                    }
                    // case 3
                    if (!flag) {
                        label = findMin();
                        img[i][j] = label;
                        if (img[i][j + 1] != 0) {
                            img[i][j + 1] = label;
                        }
                        if (img[i + 1][j - 1] != 0) {
                            img[i + 1][j - 1] = label;
                        }
                        if (img[i + 1][j] != 0) {
                            img[i + 1][j] = label;
                        }
                        if (img[i + 1][j + 1] != 0) {
                            img[i + 1][j + 1] = label;
                        }
                        updateEQ(pixel, label);
                        flag = true;
                    }
                }
            }
        }
        deBugFile.write("After connected8 pass2, newLabel = " + newLabel + "\n");
        deBugFile.write("Leaving connect8Pass2\n");
        return img;
    }

    public void connectPass3(int[][] img, Property[] CCproperty, FileWriter deBugFile) throws IOException {
        deBugFile.write("Entering connectPass3\n");
        int k = 0;
        int numPixel;
        int pixel;
        // algo in specs
        for (int i = 1; i <= this.trueNumCC; i++) {
            this.CCproperty[i].setLabel(i);
            this.CCproperty[i].setNumPixels(0);
            this.CCproperty[i].setMinR(this.numRows);
            this.CCproperty[i].setMaxR(0);
            this.CCproperty[i].setMinC(this.numCols);
            this.CCproperty[i].setMaxC(0);
        }

        for (int r = 1; r <= this.numRows; r++) {
            for (int c = 1; c <= this.numCols; c++) {
                pixel = img[r][c];
                if (pixel > 0) {
                    img[r][c] = this.EQAry[pixel];
                    k = img[r][c];
                    numPixel = this.CCproperty[k].getNumPixels();
                    CCproperty[k].setNumPixels(++numPixel);
                    if (r < this.CCproperty[k].getMinR()) {
                        this.CCproperty[k].setMinR(r-1);
                    }
                    if (r > this.CCproperty[k].getMaxR()) {
                        this.CCproperty[k].setMaxR(r-1);
                    }
                    if (c < this.CCproperty[k].getMinC()) {
                        this.CCproperty[k].setMinC(c-1);
                    }
                    if (c > this.CCproperty[k].getMaxC()) {
                        this.CCproperty[k].setMaxC(c-1);
                    }
                }
            }
        }
        deBugFile.write("Leaving connectPass3\n");
    }

    public void allocateCCproperty(int trueNumCC) {
        this.CCproperty = new Property[trueNumCC + 1];
        for (int i = 0; i <= trueNumCC; i++) {
            this.CCproperty[i] = new Property();
        }
    }

    public void drawBoxes(int[][] img, Property[] CCproperty, FileWriter deBugFile) throws IOException {
        deBugFile.write("Entering drawBoxes\n");
        int minRow, minCol, maxRow, maxCol, label;
        for (int index = 1; index <= this.trueNumCC; index++) {
            minRow = this.CCproperty[index].getMinR() + 1;
            minCol = this.CCproperty[index].getMinC() + 1;
            maxRow = this.CCproperty[index].getMaxR() + 1;
            maxCol = this.CCproperty[index].getMaxC() + 1;
            label = this.CCproperty[index].getLabel();

            for (int i = minCol; i <= maxCol; i++) {
                img[minRow][i] = label;
                img[maxRow][i] = label;
            }
            for (int i = minRow; i <= maxRow; i++) {
                img[i][minCol] = label;
                img[i][maxCol] = label;
            }
        }
        deBugFile.write("Leaviing drawBoxes\n");
    }

    public void updateEQ(int x, int y) {
        this.EQAry[x] = y;
    }

    public int manageEQAry() {
        int counter = 0;
        for (int i = 1; i < this.EQAry.length; i++) {
            if (i == this.EQAry[i]) {
                this.EQAry[i] = ++counter;
            } else {
                this.EQAry[i] = this.EQAry[this.EQAry[i]];
            }
        }
        return counter;
    }

    public void printCCproperty(BufferedWriter out) throws IOException {
        out.write(Integer.toString(this.numRows));
        out.write(" ");
        out.write(Integer.toString(this.numCols));
        out.write(" ");
        out.write(Integer.toString(this.minVal));
        out.write(" ");
        out.write(Integer.toString(this.trueNumCC));
        out.write("\n");
        out.write(Integer.toString(this.trueNumCC));
        out.write("\n");
        for (int i = 1; i <= this.trueNumCC; i++) {
            out.write(Integer.toString(this.CCproperty[i].getLabel()));
            out.write("\n");
            out.write(Integer.toString(this.CCproperty[i].getNumPixels()));
            out.write("\n");
            out.write(Integer.toString(this.CCproperty[i].getMinR()));
            out.write(" ");
            out.write(Integer.toString(this.CCproperty[i].getMinC()));
            out.write("\n");
            out.write(Integer.toString(this.CCproperty[i].getMaxR()));
            out.write(" ");
            out.write(Integer.toString(this.CCproperty[i].getMaxC()));
            out.write("\n");
        }
    }

    public void printEQAry(BufferedWriter out) throws IOException {
        for (int i = 1; i <= this.newLabel; i++) {
            out.write(Integer.toString(this.EQAry[i]));
            out.write(" ");
        }
        out.write("\n");
    }

    public void printImg(BufferedWriter outFile) throws IOException {
        String str = Integer.toString(this.maxVal);
        int width = str.length();

        for (int r = 1; r <= this.numRows; r++) {
            for (int c = 1; c <= this.numCols; c++) {
                if(this.zeroFramedAry[r][c] > 0){
                    outFile.write(Integer.toString(this.zeroFramedAry[r][c]));
                } else {
                    outFile.write(".");
                }
                String str2 = Integer.toString(this.zeroFramedAry[r][c]);
                int WW = str2.length();
                outFile.write(" ");
                while (WW < width) {
                    outFile.write(" ");
                    WW++;
                }
            }
            outFile.write("\n");
        }
    }

    private int findMin() {
        int retVal = this.nonZeroNeighborAry[0];
        for (int i = 1; i < this.nonZeroNeighborAry.length; i++) {
            if (retVal > this.nonZeroNeighborAry[i] && this.nonZeroNeighborAry[i] != -1) {
                retVal = this.nonZeroNeighborAry[i];
            }
        }
        this.nonZeroNeighborAry = minus1D(this.nonZeroNeighborAry);
        return retVal;
    }

    // setter/getter here
    public void setTrueNum(int i) {
        this.trueNumCC = i;
    }

    public void setNewMin(int i) {
        this.newMin = i;
    }

    public void setNewMax(int i) {
        this.newMax = i;
    }

    public int getTrueNum() {
        return this.trueNumCC;
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumCols() {
        return this.numCols;
    }

    public int getNewMin() {
        return this.newMin;
    }

    public int getNewMax() {
        return this.newMax;
    }
}
