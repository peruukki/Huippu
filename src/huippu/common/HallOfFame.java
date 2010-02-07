package huippu.common;

public abstract class HallOfFame
{
    protected static final String STORE_SCORES_LEVEL = "ScoresLevel";
    protected static final String STORE_SCORES_TOTAL = "ScoresTotal";
    protected static final String STORE_REMOVES_LEVEL = "RemovesLevel";
    
    protected StoreIntDescending mScoresLevel =
        new StoreIntDescending( Scores.getInitialScoresLevel() );
    protected StoreIntDescending mScoresTotal =
        new StoreIntDescending( Scores.getInitialScoresTotal() );
    protected StoreIntAscending mRemovesLevel =
        new StoreIntAscending( Scores.getInitialRemovesLevel() );
    
    public boolean addScoreLevel( final int pScore )
    {
        return mScoresLevel.addValue( pScore );
    }
    
    public boolean addScoreTotal( final int pScore )
    {
        return mScoresTotal.addValue( pScore );
    }
    
    public boolean addRemovesLevel( final int pCount )
    {
        return mRemovesLevel.addValue( pCount );
    }
    
    public final int[] getScoresLevel()
    {
        return mScoresLevel.getValues();
    }
    
    public final int[] getScoresTotal()
    {
        return mScoresTotal.getValues();
    }
    
    public final int[] getRemovesLevel()
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
