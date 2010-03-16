package huippu.common;

import java.io.DataInputStream;
import java.io.IOException;

public final class Score
    implements IStorable
{
    private static final boolean mUseAllZero = false;
    
    private final int mValue;
    private final int mLevel;
    private final ScoreDate mDate;
    
    public Score()
    {
        this( 0, 0, new ScoreDate() );
    }
    
    public Score( final int pValue, final int pLevel, final ScoreDate pDate )
    {
        mValue = pValue;
        mLevel = pLevel;
        mDate = pDate;
    }

    public Score( final int pValue, final int pLevel )
    {
        mValue = pValue;
        mLevel = pLevel;
        mDate = new ScoreDate();
    }
    
    public Score( final DataInputStream pInput )
        throws IOException
    {
        mValue = pInput.readInt();
        mLevel = pInput.readInt();
        mDate = new ScoreDate( pInput.readByte(),
                               pInput.readByte(),
                               pInput.readByte() );
    }
    
    public byte[] getAsBytes()
        throws IOException
    {
        // Convert all values to byte data
        final byte[] dataValue = Storable.getDataInt( mValue );
        final byte[] dataLevel = Storable.getDataInt( mLevel );
        final byte[] dataDate = mDate.getAsBytes();
        
        // Append all data to a continuous byte array
        final byte[] data = new byte[   dataValue.length
                                      + dataLevel.length
                                      + dataDate.length ];
        
        int offset = 0;
        
        offset = Storable.appendData( dataValue, data, offset );
        offset = Storable.appendData( dataLevel, data, offset );
        offset = Storable.appendData( dataDate, data, offset );
        
        return data;
    }
    
    public int getValue()
    {
        return mValue;
    }
    
    public int getLevel()
    {
        return mLevel;
    }
    
    public ScoreDate getDate()
    {
        return mDate;
    }
    
    private static final Score[] mScoresTotal = new Score[]
    {
        new Score( 588, 6, new ScoreDate( 20,  2, 10 ) ),
        new Score( 512, 5, new ScoreDate( 20,  2, 10 ) ),
        new Score( 507, 5, new ScoreDate( 12,  3, 10 ) ),
        new Score( 440, 4, new ScoreDate(  9,  3, 10 ) ),
        new Score( 431, 4, new ScoreDate( 22,  2, 10 ) ),
    };
    
    private static final Score[] mScoresLevelBiggest = new Score[]
    {
        new Score( 133, 1, new ScoreDate(  9,  2, 10 ) ),
        new Score( 127, 1, new ScoreDate( 22,  2, 10 ) ),
        new Score( 127, 1, new ScoreDate( 22,  2, 10 ) ),
        new Score( 125, 1, new ScoreDate( 10,  2, 10 ) ),
        new Score( 124, 1, new ScoreDate(  9,  3, 10 ) ),
    };
    
    private static final Score[] mScoresLevelSmallest = new Score[]
    {
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
    };
    
    private static final Score[] mRemovesLevelSmallest = new Score[]
    {
        new Score(  7,  1, new ScoreDate(  9,  2, 10 ) ),
        new Score(  9,  1, new ScoreDate( 22,  2, 10 ) ),
        new Score(  9,  1, new ScoreDate( 22,  2, 10 ) ),
        new Score( 10,  1, new ScoreDate(  9,  3, 10 ) ),
        new Score( 10,  1, new ScoreDate( 22,  2, 10 ) ),
    };
    
    private static final Score[] mRemovesLevelBiggest = new Score[]
    {
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
        new Score(  0,  1, new ScoreDate( 16,  3, 10 ) ),
    };
    
    private static final Score[] mRemovesBiggest = new Score[]
    {
        new Score( 30,  1, new ScoreDate( 12,  3, 10 ) ),
        new Score( 27,  1, new ScoreDate(  9,  3, 10 ) ),
        new Score( 26,  3, new ScoreDate(  8,  3, 10 ) ),
        new Score( 24,  2, new ScoreDate(  9,  3, 10 ) ),
        new Score( 24,  1, new ScoreDate(  8,  3, 10 ) ),
    };

    static final Score[] getInitialScoresTotal()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mScoresTotal.length );
        }
        else
        {
            return mScoresTotal;
        }
    }
    
    static final Score[] getInitialScoresLevelBiggest()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mScoresLevelBiggest.length );
        }
        else
        {
            return mScoresLevelBiggest;
        }
    }
    
    static final Score[] getInitialScoresLevelSmallest()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mScoresLevelSmallest.length );
        }
        else
        {
            return mScoresLevelSmallest;
        }
    }
    
    static final Score[] getInitialRemovesLevelSmallest()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mRemovesLevelSmallest.length );
        }
        else
        {
            return mRemovesLevelSmallest;
        }
    }
    
    static final Score[] getInitialRemovesLevelBiggest()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mRemovesLevelBiggest.length );
        }
        else
        {
            return mRemovesLevelBiggest;
        }
    }
    
    static final Score[] getInitialRemovesBiggest()
    {
        if ( mUseAllZero )
        {
            return getEmptyScores( mRemovesBiggest.length );
        }
        else
        {
            return mRemovesBiggest;
        }
    }
    
    static final Score[] getEmptyScores( final int pSize )
    {
        final Score[] scores = new Score[ pSize ];
        for ( int i = 0; i < scores.length; i++ )
        {
            scores[ i ] = new Score();
        }
        return scores;
    }
}
