package huippu.common;

public final class ScoresIntAscending
    extends ScoresInt
{
    public ScoresIntAscending( final Score[] pInitialValues )
    {
        super( pInitialValues );
    }

    protected final int getIndexToAdd( final Score pValue )
    {
        int index = -1;

        final int newValue = pValue.getValue();
        if ( newValue != 0 )
        {
            index = 0;
            for ( int i = mValues.length - 1; index == 0 && i >= 0; i-- )
            {
                final int existingValue = mValues[ i ].getValue();
                if (    existingValue != 0
                     && newValue > existingValue )
                {
                    index = i + 1;
                }
            }
            index = ( index == mValues.length ) ? -1 : index;
        }

        return index;
    }
}
