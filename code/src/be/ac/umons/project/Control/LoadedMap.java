package be.ac.umons.project.Control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LoadedMap {
    private String[][] interMatrix;

    /***************************************************************
     * Constructor
     * @param file  (File) from where map should be loaded
     *@throws Exception
     ***************************************************************/
    public LoadedMap(File file)throws Exception{
        createInterMatrix(file);
    }

    ////////////////////////////////////////////////
    //GETTERS
    ////////////////////////////////////////////////
    /***************************************************************
     *@return (String[][]) intermediate matrix of this loaded map
     ***************************************************************/
    public String[][] getInterMatrix() {
        return interMatrix;
    }


    ////////////////////////////////////////////////
    //METHODS
    ////////////////////////////////////////////////
    /***************************************************************
     *transfers data from selected file to the interMatrix
     * @param file (File) file from which to import map
     * @throws Exception
     ***************************************************************/
    private void createInterMatrix(File file)throws Exception{
        int[] dim = checkDimensions(file);

        if ((dim[0] == 0) || (dim[0] > 26) || (dim[1] == 0) || (dim[1] > 26)) {
            throw new BadMapException("Dimensions of board must be >0 & <27");
        }
        interMatrix = new String[dim[1]][dim[0]];

        int y = 0;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String thisLine = br.readLine();
        while (thisLine != null) {
            String[] row = new String[thisLine.length()];
            for (int x = 0; x < thisLine.length(); x++) {
                char c = thisLine.charAt(x);
                if ((c != '.') && (c != 'X')) {
                    throw new BadMapException
                            ("File contains characters other than '.' and 'X'");
                }
                row[x] = Character.toString(c);
            }
            interMatrix[y] = row;
            thisLine = br.readLine();
            y += 1;
        }
    }


    /***************************************************************
     *@param file (File) containing board setup
     *@return (int[]) where int[0] = width of board, int[1] = height of board
     *@throws Exception
     ***************************************************************/
    private int[] checkDimensions(File file)throws Exception{
        int[] ans = new int[2];
        BufferedReader br = new BufferedReader(new FileReader(file));
        String thisLine = br.readLine();
        int y = 0;
        int x = thisLine.length();
        while (thisLine != null) {
            y += 1;
            if (thisLine.length() != x) {
                throw new BadMapException("Your file doesn't have all rows of same length");
            }
            thisLine = br.readLine();
        }
        ans[1] = y;
        ans[0] = x;
        return ans;
    }




}
