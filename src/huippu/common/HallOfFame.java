package huippu.common;

public abstract class HallOfFame
{
    protected static final String STORE_SCORES_LEVEL = "ScoresLevel";
    protected static final String STORE_SCORES_TOTAL = "ScoresTotal";
    protected static final String STORE_REMOVES_LEVEL_SMALLEST = "RemovesLevelSmallest";
    protected static final String STORE_REMOVES_LEVEL_BIGGEST = "RemovesLevelBiggest";
    protected static final String STORE_REMOVES_BIGGEST = "RemovesBiggest";
    
    protected StoreIntDescending mScoresLevel =
        new StoreIntDescending( Score.getInitialScoresLevel() );
    protected StoreIntDescending mScoresTotal =
        new StoreIntDescending( Score.getInitialScoresTotal() );
    protected StoreIntAscending mRemovesLevelSmallest =
        new StoreIntAscending( Score.getInitialRemovesLevelSmallest() );
    protected StoreIntDescending mRemovesLevelBiggest =
        new StoreIntDescending( Score.getInitialRemovesLevelBiggest() );
    protected StoreIntDescending mRemovesBiggest =
        new StoreIntDescending( Score.getInitialRemovesBiggest() );
    
    public boolean addScoreLevel( final Score pScore )
    {
        return mScoresLevel.addValue( pScore );
    }
    
    public boolean addScoreTotal( final Score pScore )
    {
        return mScoresTotal.addValue( pScore );
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
    
    public final Score[] getScoresLevel()
    {
        return mScoresLevel.getValues();
    }
    
    public final Score[] getScoresTotal()
    {
        return mScoresTotal.getValues();
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
        mScoresLevel.clear();
        mScoresTotal.clear();
        mRemovesLevelSmallest.clear();
        mRemovesLevelBiggest.clear();
        mRemovesBiggest.clear();
    }
    
    abstract public void showHallOfFame();
}
