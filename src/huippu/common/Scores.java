package huippu.common;

public final class Scores
{
    private static final boolean mUseAllZero = true;
    
    private static final short[] mScoresTotal = new short[]
    {
        50, 40, 30, 20, 10
    };
    
    private static final short[] mScoresLevel = new short[]
    {
        20, 19, 18, 17, 0
    };

    private static final float[] mRemovesAvg = new float[]
    {
        (float) 18.0, (float) 19.0, (float) 20.0, (float) 21.0, (float) 22.0
    };
    
    private static final byte[] mRemovesLevel = new byte[]
    {
        11, 12, 13, 14, 0
    };
    
    static final short[] getInitialScoresTotal()
    {
        if ( mUseAllZero )
        {
            return new short[ mScoresTotal.length ];
        }
        else
        {
            return mScoresTotal;
        }
    }
    
    static final short[] getInitialScoresLevel()
    {
        if ( mUseAllZero )
        {
            return new short[ mScoresLevel.length ];
        }
        else
        {
            return mScoresLevel;
        }
    }
    
    static final float[] getInitialRemovesAvg()
    {
        if ( mUseAllZero )
        {
            return new float[ mRemovesAvg.length ];
        }
        else
        {
            return mRemovesAvg;
        }
    }
    
    static final byte[] getInitialRemovesLevel()
    {
        if ( mUseAllZero )
        {
            return new byte[ mRemovesLevel.length ];
        }
        else
        {
            return mRemovesLevel;
        }
    }
}
