package de.lmu.dbs.jfeaturelib.features;

import de.lmu.ifi.dbs.utilities.Arrays2;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import java.util.EnumSet;

/**
 * Reads the histogram from the Image Processor and returns it as double[].      
 * @author Benedikt
 */
public class GrayValueHistogram implements FeatureDescriptor{

    private long time;
    private boolean calculated;
    private int progress; 
    private int TONAL_VALUES;
    private int[] features;
    private ByteProcessor image;

    /**
     * Constructs a histogram with default parameters.
     */     
    public GrayValueHistogram(){
        //assuming 8bit image
        TONAL_VALUES = 256;
        features = new int[TONAL_VALUES];
        calculated = false;
        progress = 0;
    }
    
    /**
     * Constructs a histogram.
     * @param values Number of tonal values, i.e. 256 for 8bit jpeg
     */  
    
    public GrayValueHistogram(int values){
        TONAL_VALUES = values;
        features = new int[TONAL_VALUES];
    }
        
    /**
    * Returns the histogram as int array.
    */    
    @Override
    public double[] getFeatures() {
        if(calculated){
            return Arrays2.convertToDouble(features);
        }
        else{
            //TODO throw exception
            return null;
        }
    }
    
    /**
     * Defines the capability of the algorithm.
     * 
     * @see PlugInFilter
     * @see #supports() 
     */
    @Override
    public EnumSet<Supports> supports() {        
        EnumSet set = EnumSet.of(
            Supports.NoChanges,
            Supports.DOES_8C,
            Supports.DOES_8G,
            Supports.DOES_RGB
        );
        //set.addAll(DOES_ALL);
        return set;
    }
    
    /**
     * Starts the histogram detection.
     * @param ip ImageProcessor of the source image
     */ 
    @Override
    public void run(ImageProcessor ip) {
        long start = System.currentTimeMillis();
        if (!ByteProcessor.class.isAssignableFrom(ip.getClass())) {
            ip = ip.convertToByte(true);
        }
        this.image = (ByteProcessor) ip;
        process();
        calculated = true;
        time = (System.currentTimeMillis() - start);
    }
    
    /**
     * Returns information about the getFeauture returns in a String array.
     */     
    @Override
    public String[] getDescription() {
        String[] info =  new String[TONAL_VALUES];
        for (int i = 0; i < info.length; i++){
            info[i] = "Pixels with tonal value " + i;
        }
        return(info);
    }
    
    private void process() {
        features = image.getHistogram();
        progress = 100;
    }
    
    public long getTime(){
         return time;
     }
    
    public boolean isCalculated(){
        return calculated;
    }

    @Override
    public int getProgress() {
        return progress;
    }
}
