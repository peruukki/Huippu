package huippu.common;

public abstract class HallOfFame
{
    protected static final String STORE_SCORES_LEVEL = "ScoresLevel";
    protected static final String STORE_SCORES_TOTAL = "ScoresTotal";
    protected static final String STORE_REMOVES_LEVEL = "RemovesLevel";
    
    protected StoreIntDescending mScoresLevel =
        new StoreIntDescending( Score.getInitialScoresLevel() );
    protected StoreIntDescending mScoresTotal =
        new StoreIntDescending( Score.getInitialScoresTotal() );
    protected StoreIntAscending mRemovesLevel =
        new StoreIntAscending( Score.getInitialRemovesLevel() );
    
    public boolean addScoreLevel( final Score pScore )
    {
        return mScoresLevel.addValue( pScore );
    }
    
    public boolean addScoreTotal( final Score pScore )
    {
        return mScoresTotal.addValue( pScore );
    }
    
    public boolean addRemovesLevel( final Score pScore )
    {
        return mRemovesLevel.addValue( pScore );
    }
    
    public final Score[] getScoresLevel()
    {
        return mScoresLevel.getValues();
    }
    
    public final Score[] getScoresTotal()
    {
        return mScoresTotal.getValues();
    }
    
    public final Score[] getRemovesLevel()
    {
        return mRemovesLevel.getValues();
    }
    
    public void clearAll()
    {
        mScoresLevel.clear();
        mScoresTotal.clear();
        mRemovesLevel.clear();
    }
    
    abstract public void showHallOfFame();
}
