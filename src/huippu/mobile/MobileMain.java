package huippu.mobile;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public final class MobileMain
    extends MIDlet
{
    private Thread mThread = null;
    private final MobileDrome mDrome;
    
    private static Displayable mCurrentScreen = null;
    private static MIDlet mMIDlet;
    
    public MobileMain()
    {
        mDrome = new MobileDrome( this );
        mMIDlet = this;
    }

    protected final void destroyApp( final boolean arg0 )
//		throws MIDletStateChangeException
    {
    //        theGame.destroyGame();
        Display.getDisplay( this )
               .setCurrent( null );
    }

    protected final void pauseApp()
    {
        // Stop game's timing thread
//        theGame.destroyGame();

        try
        {
            // Start another thread after this one finishes
            mThread.join();
        }
        catch ( final InterruptedException e )
        {
            // Ignored
        }
    }

    protected final void startApp()
        throws MIDletStateChangeException
    {
        Display.getDisplay( this )
               .setCurrent( mDrome );

        try
        {
            // Start the game in its own thread
            mThread = new Thread( mDrome );
        	
            // Ensure the game thread will work after pause
//            theGame.setDestroyed( false );
            mThread.start();
        }
        catch ( final Error e )
        {
            exitApplication();
        }
//        if ( !theGame.isStarted() )
//        {
//            theGame.restart();
//        }
    }
    
    final void exitApplication()
    {
        destroyApp( false );
        notifyDestroyed();
    }
    
    public static void error( final String pMsg, final Throwable pE )
    {
        mCurrentScreen = Display.getDisplay( mMIDlet ).getCurrent();
        final Alert alert = new Alert( "Error",
                                       pMsg + ": " + pE.toString(),
                                       null,
                                       AlertType.ERROR );
        alert.addCommand( new Command( "OK", Command.OK, 1 ) );
        alert.setCommandListener( new CommandListener()
        {
            public void commandAction( final Command pC, final Displayable pD )
            {
                closeAlert();
            }
        });
        Display.getDisplay( mMIDlet ).setCurrent( alert, mCurrentScreen );
    }

    private static final void closeAlert()
    {
        Display.getDisplay( mMIDlet ).setCurrent( mCurrentScreen );
    }
}
