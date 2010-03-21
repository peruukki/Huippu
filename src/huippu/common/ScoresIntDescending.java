package huippu.common;

public final class ScoresIntDescending
    extends ScoresInt
{
    public ScoresIntDescending( final Score[] pInitialValues )
    {
        super( pInitialValues );
    }

    protected final int getIndexToAdd( final Score pValue )
    {
        int index = 0;

        for ( int i = mValues.length - 1; index == 0 && i >= 0; i-- )
        {
            if ( pValue.getValue() < mValues[ i ].getValue() )
            {
                index = i + 1;
            }
        }

        return ( index == mValues.length ) ? -1 : index;
    }
 }
