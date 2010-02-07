package huippu.common;

public abstract class HallOfFame
{
    protected static final String STORE_SCORES_LEVEL = "ScoresLevel";
    protected static final String STORE_SCORES_TOTAL = "ScoresTotal";
    protected static final String STORE_REMOVES_LEVEL = "RemovesLevel";
    
    protected StoreShortDescending mScoresLevel =
        new StoreShortDescending( Scores.getInitialScoresLevel() );
    protected StoreShortDescending mScoresTotal =
        new StoreShortDescending( Scores.getInitialScoresTotal() );
    protected StoreByteAscending mRemovesLevel =
        new StoreByteAscending( Scores.getInitialRemovesLevel() );
    
    public boolean addScoreLevel( final short pScore )
    {
        return mScoresLevel.addValue( pScore );
    }
    
    public boolean addScoreTotal( final short pScore )
    {
        return mScoresTotal.addValue( pScore );
    }
    
    public boolean addRemovesLevel( final byte pCount )
    {
        return mRemovesLevel.addValue( pCount );
    }
    
    public final short[] getScoresLevel()
    {
        return mScoresLevel.getValues();
    }
    
    public final short[] getScoresTotal()
    {
        return mScoresTotal.getValues();
    }
    
    public final byte[] getRemovesLevel()
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
