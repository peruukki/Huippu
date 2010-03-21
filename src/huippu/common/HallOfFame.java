package huippu.common;

public abstract class HallOfFame
{
    protected static final String STORE_SCORES_TOTAL_BIGGEST = "ScoresTotalBiggest";
    protected static final String STORE_SCORES_TOTAL_SMALLEST = "ScoresTotalSmallest";
    protected static final String STORE_SCORES_LEVEL_BIGGEST = "ScoresLevelBiggest";
    protected static final String STORE_SCORES_LEVEL_SMALLEST = "ScoresLevelSmallest";
    protected static final String STORE_REMOVES_LEVEL_SMALLEST = "RemovesLevelSmallest";
    protected static final String STORE_REMOVES_LEVEL_BIGGEST = "RemovesLevelBiggest";
    protected static final String STORE_REMOVES_BIGGEST = "RemovesBiggest";
    
    protected ScoresIntDescending mScoresTotalBiggest =
        new ScoresIntDescending( Score.getInitialScoresTotalBiggest() );
    protected ScoresIntAscending mScoresTotalSmallest =
        new ScoresIntAscending( Score.getInitialScoresTotalSmallest() );
    protected ScoresIntDescending mScoresLevelBiggest =
        new ScoresIntDescending( Score.getInitialScoresLevelBiggest() );
    protected ScoresIntAscending mScoresLevelSmallest =
        new ScoresIntAscending( Score.getInitialScoresLevelSmallest() );
    protected ScoresIntAscending mRemovesLevelSmallest =
        new ScoresIntAscending( Score.getInitialRemovesLevelSmallest() );
    protected ScoresIntDescending mRemovesLevelBiggest =
        new ScoresIntDescending( Score.getInitialRemovesLevelBiggest() );
    protected ScoresIntDescending mRemovesBiggest =
        new ScoresIntDescending( Score.getInitialRemovesBiggest() );
    
    public boolean addScoreTotalBiggest( final Score pScore )
    {
        return mScoresTotalBiggest.addValue( pScore );
    }
    
    public boolean addScoreTotalSmallest( final Score pScore )
    {
        return mScoresTotalSmallest.addValue( pScore );
    }
    
    public boolean addScoreLevelBiggest( final Score pScore )
    {
        return mScoresLevelBiggest.addValue( pScore );
    }
    
    public boolean addScoreLevelSmallest( final Score pScore )
    {
        return mScoresLevelSmallest.addValue( pScore );
    }
    
    public boolean addRemovesLevelSmallest( final Score pScore )
    {
        return mRemovesLevelSmallest.addValue( pScore );
    }
    
    public boolean addRemovesLevelBiggest( final Score pScore )
    {
        return mRemovesLevelBiggest.addValue( pScore );
    }
    
    public boolean addRemoveBiggest( final Score pScore )
    {
        return mRemovesBiggest.addValue( pScore );
    }
    
    public final Score[] getScoresTotalBiggest()
    {
        return mScoresTotalBiggest.getValues();
    }
    
    public final Score[] getScoresTotalSmallest()
    {
        return mScoresTotalSmallest.getValues();
    }
    
    public final Score[] getScoresLevelBiggest()
    {
        return mScoresLevelBiggest.getValues();
    }
    
    public final Score[] getScoresLevelSmallest()
    {
        return mScoresLevelSmallest.getValues();
    }
    
    public final Score[] getRemovesLevelSmallest()
    {
        return mRemovesLevelSmallest.getValues();
    }
    
    public final Score[] getRemovesLevelBiggest()
    {
        return mRemovesLevelBiggest.getValues();
    }
    
    public final Score[] getRemovesBiggest()
    {
        return mRemovesBiggest.getValues();
    }
    
    public void clearAll()
    {
        mScoresTotalBiggest.clear();
        mScoresTotalSmallest.clear();
        mScoresLevelBiggest.clear();
        mScoresLevelSmallest.clear();
        mRemovesLevelSmallest.clear();
        mRemovesLevelBiggest.clear();
        mRemovesBiggest.clear();
    }
    
    abstract public void showHallOfFame();
}
