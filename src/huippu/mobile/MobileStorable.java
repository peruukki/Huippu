package huippu.mobile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

import huippu.common.Storable;

abstract class MobileStorable extends Storable
{
    protected static final RecordStore openStore( final String pStoreName,
                                                  final boolean pCreateIfNotExist,
                                                  final boolean pMustExist )
    {
        RecordStore store = null;
        try
        {
            store = RecordStore.openRecordStore( pStoreName, pCreateIfNotExist );
        }
        catch ( final RecordStoreNotFoundException e )
        {
            if ( pMustExist )
            {
                MobileMain.error( "Failed to open store " + pStoreName, e );
                closeStore( store );
                store = null;
            }
        }
        catch ( final RecordStoreException e )
        {
            MobileMain.error( "Failed to open store " + pStoreName, e );
            closeStore( store );
            store = null;
        }
        return store;
    }
    
    protected static final void closeStore( final RecordStore pStore )
    {
        if ( pStore != null )
        {
            try
            {
                pStore.closeRecordStore();
            }
            catch ( final RecordStoreException e )
            {
                MobileMain.error( "Failed to close store " + pStore, e );
            }
        }
    }
    
    protected final static void deleteStore( final String pStoreName )
    {
        try
        {
            RecordStore.deleteRecordStore( pStoreName );
        }
        catch ( final RecordStoreNotFoundException e )
        {
            // OK, do nothing
        }
        catch ( final RecordStoreException e )
        {
            MobileMain.error( "Failed to delete store " + pStoreName, e );
        }
    }
    
    protected static final DataInputStream getDataStream( final RecordStore pStore,
                                                          final int pRecordId )
        throws RecordStoreException
    {
        DataInputStream dis = null;
        
        final byte[] data = pStore.getRecord( pRecordId );
        if ( data != null )
        {
            dis = new DataInputStream( new ByteArrayInputStream( data ) );
        }
        
        return dis;
    }
}
