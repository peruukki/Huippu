package huippu.common;

public final class Scores
{
    private static final boolean mUseAllZero = true;
    
    private static final int[] mScoresTotal = new int[]
    {
        50, 40, 30, 20, 10
    };
    
    private static final int[] mScoresLevel = new int[]
    {
        20, 19, 18, 17, 0
    };
    
    private static final int[] mRemovesLevel = new int[]
    {
        11, 12, 13, 14, 0
    };
    
    static final int[] getInitialScoresTotal()
    {
        if ( mUseAllZero )
        {
            return new int[ mScoresTotal.length ];
        }
        else
        {
            return mScoresTotal;
        }
    }
    
    static final int[] getInitialScoresLevel()
    {
        if ( mUseAllZero )
        {
            return new int[ mScoresLevel.length ];
        }
        else
        {
            return mScoresLevel;
        }
    }
    
    static final int[] getInitialRemovesLevel()
    {
        if ( mUseAllZero )
        {
            return new int[ mRemovesLevel.length ];
        }
        else
        {
            return mRemovesLevel;
        }
    }
}
