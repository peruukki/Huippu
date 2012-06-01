package huippu.mobile;

import huippu.common.Resources;

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
    private transient final MobileDrome mDrome;
    
    private static Displayable mCurrentScreen = null;
    private static MIDlet mMIDlet;
    private static ConfirmationReceiver mConfirmationReceiver = null;
    private static Command mConfirmedCommand = null;
    
    public MobileMain()
    {
        mDrome = new MobileDrome( this );
        mMIDlet = this;
    }

    protected final void destroyApp( final boolean arg0 )
    {
        mDrome.gameExiting();
        Display.getDisplay( this )
               .setCurrent( null );
    }

    protected final void pauseApp()
    {
        // TODO
    }

    protected final void startApp()
        throws MIDletStateChangeException
    {
        Display.getDisplay( this )
               .setCurrent( mDrome );

        try
        {
            // Start the game in its own thread
            new Thread( mDrome )
                .start();
        }
        catch ( final Error e )
        {
            exitApplication();
        }
    }
    
    final void exitApplication()
    {
        destroyApp( false );
        notifyDestroyed();
    }

    public static void confirm( final String pMsg,
                                final ConfirmationReceiver pReceiver,
                                final Command pCommand )
    {
        mConfirmationReceiver = pReceiver;
        mConfirmedCommand = pCommand;
        mCurrentScreen = Display.getDisplay( mMIDlet ).getCurrent();
        
        final Alert alert = new Alert( Resources.TITLE_CONFIRM,
                                       pMsg,
                                       null,
                                       AlertType.CONFIRMATION );
        alert.addCommand( new Command( Resources.TEXT_YES, Command.OK, 1 ) );
        alert.addCommand( new Command( Resources.TEXT_NO, Command.CANCEL, 2 ) );
        alert.setCommandListener( new CommandListener()
        {
            public void commandAction( final Command pC, final Displayable pD )
            {
                closeConfirmation( pC );
            }
        });
        
        Display.getDisplay( mMIDlet ).setCurrent( alert, mCurrentScreen );
    }
    
    private static final void closeConfirmation( final Command pC )
    {
        Display.getDisplay( mMIDlet ).setCurrent( mCurrentScreen );
        mConfirmationReceiver.handleResult( mConfirmedCommand,
                                            pC.getCommandType() );
    }
    
    public static void error( final String pMsg, final Throwable pE )
    {
        mCurrentScreen = Display.getDisplay( mMIDlet )
                                .getCurrent();
        
        String message = pMsg;
        if ( pE != null )
        {
            message += ": " + pE;
        }
        
        final Alert alert = new Alert( "Error",
                                       message,
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
        
        Display.getDisplay( mMIDlet )
               .setCurrent( alert, mCurrentScreen );
    }

    private static final void closeAlert()
    {
        Display.getDisplay( mMIDlet )
               .setCurrent( mCurrentScreen );
    }
}
