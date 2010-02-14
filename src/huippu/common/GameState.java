package huippu.common;


public final class GameState
{
    private final DudeGrid mDudeGrid;

    private int mScoreLevel = 0;
    private int mScoreTotal = 0;
    private int mRemoveCountLevel = 0;
    
    private int mLevel = 1;
    
    public GameState( final DudeGrid pDudeGrid )
    {
        mDudeGrid = pDudeGrid;
    }
    
    public final DudeGrid getDudeGrid()
    {
        return mDudeGrid;
    }

    public final int getScoreLevel()
    {
        return mScoreLevel;
    }

    public final void setScoreLevel( final int pScoreLevel )
    {
        mScoreLevel = pScoreLevel;
    }
    
    public final void incrementScoreLevel( final int pIncrement )
    {
        mScoreLevel += pIncrement;
    }

    public final int getScoreTotal()
    {
        return mScoreTotal;
    }

    public final void setScoreTotal( final int pScoreTotal )
    {
        mScoreTotal = pScoreTotal;
    }
    
    public final void incrementScoreTotal( final int pIncrement )
    {
        mScoreTotal += pIncrement;
    }

    public final int getRemoveCountLevel()
    {
        return mRemoveCountLevel;
    }

    public final void setRemoveCountLevel( final int pRemoveCountLevel )
    {
        mRemoveCountLevel = pRemoveCountLevel;
    }
    
    public final void incrementRemoveCountLevel()
    {
        mRemoveCountLevel++;
    }

    public final int getLevel()
    {
        return mLevel;
    }

    public final void setLevel( final int pLevel )
    {
        mLevel = pLevel;
    }
    
    public final void incrementLevel()
    {
        mLevel++;
    }
}
