package huippu.common;

public abstract class HallOfFame
{
    protected static final String STORE_SCORES_TOTAL = "ScoresTotal";
    protected static final String STORE_SCORES_LEVEL_BIGGEST = "ScoresLevelBiggest";
    protected static final String STORE_SCORES_LEVEL_SMALLEST = "ScoresLevelSmallest";
    protected static final String STORE_REMOVES_LEVEL_SMALLEST = "RemovesLevelSmallest";
    protected static final String STORE_REMOVES_LEVEL_BIGGEST = "RemovesLevelBiggest";
    protected static final String STORE_REMOVES_BIGGEST = "RemovesBiggest";
    
    protected StoreIntDescending mScoresTotal =
        new StoreIntDescending( Score.getInitialScoresTotal() );
    protected StoreIntDescending mScoresLevelBiggest =
        new StoreIntDescending( Score.getInitialScoresLevelBiggest() );
    protected StoreIntAscending mScoresLevelSmallest =
        new StoreIntAscending( Score.getInitialScoresLevelSmallest() );
    protected StoreIntAscending mRemovesLevelSmallest =
        new StoreIntAscending( Score.getInitialRemovesLevelSmallest() );
    protected StoreIntDescending mRemovesLevelBiggest =
        new StoreIntDescending( Score.getInitialRemovesLevelBiggest() );
    protected StoreIntDescending mRemovesBiggest =
        new StoreIntDescending( Score.getInitialRemovesBiggest() );
    
    public boolean addScoreTotal( final Score pScore )
    {
        return mScoresTotal.addValue( pScore );
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
    
    public final Score[] getScoresTotal()
    {
        return mScoresTotal.getValues();
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
        mScoresTotal.clear();
        mScoresLevelBiggest.clear();
        mScoresLevelSmallest.clear();
        mRemovesLevelSmallest.clear();
        mRemovesLevelBiggest.clear();
        mRemovesBiggest.clear();
    }
    
    abstract public void showHallOfFame();
}
