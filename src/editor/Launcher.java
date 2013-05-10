package editor;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import engine.Engine;

public class Launcher extends Engine {
	
	private final String APP_NAME = "ViewXML Editor";
	
	private JFrame frame = null;
	private File currentFile = null;
	private JTextPane editor = null;
	private JTextArea console = null;
	private JLabel line = null;
	private JMenuItem save;
	private JMenuItem saveAs;
	private JMenuItem run;
	private JMenuItem undo;
	private JMenuItem redo;
	private boolean changed = false;
	
	private void confirm() {
		if(!this.changed)
			return;
		String message = "This document has been modified. Would like to save your changes?";	
		int reply = JOptionPane.showConfirmDialog(null, message, this.frame.getTitle(), JOptionPane.YES_NO_OPTION);
		if(reply == JOptionPane.YES_OPTION) {
			this.saveFile();
		}
	}
	
	private void saveFile() {
		if (this.currentFile == null) {
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new SimpleFileFilter(".xml"));
			int returnVal = fc.showSaveDialog(this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if(!fc.getSelectedFile().getName().endsWith(".xml"))
					this.currentFile = new File(fc.getSelectedFile().getAbsolutePath() + ".xml");
				else
					this.currentFile = fc.getSelectedFile();
				this.frame.setTitle("*" + this.APP_NAME + " " +  this.currentFile.getName());
			}
			else
				return;
		}
		try {
			FileWriter out = new FileWriter(this.currentFile);
			this.editor.write(out);
			out.close();
			this.changed = false;
			this.save.setEnabled(false);
			Launcher.this.frame.setTitle(Launcher.this.frame.getTitle().substring(1));
		} catch (IOException ex) {
			//showError(ex, "Error saving file " + currentFile);
		}			
	}
	
	private void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				console.append(text);
		    }
		});
	}
		 
	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
				}
		 
			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}
		 
			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};
		 
		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}
	
	private int getLineOfOffset(JTextPane comp, int offset) throws BadLocationException {
	    Document doc = comp.getDocument();
	    if (offset < 0) {
	        throw new BadLocationException("Can't translate offset to line", -1);
	    } else if (offset > doc.getLength()) {
	        throw new BadLocationException("Can't translate offset to line", doc.getLength() + 1);
	    } else {
	        Element map = doc.getDefaultRootElement();
	        return map.getElementIndex(offset) + 1;
	    }
	}
		
	public Launcher() {
		this.setLayout(Launcher.class.getResourceAsStream("resources/main.xml"));
		this.frame = (JFrame) this.findViewById("main");
		
		this.editor = (JTextPane) this.findViewById("text");
		this.editor.addKeyListener(new EditorListener());
		UndoManager manager = new UndoManager();
	    this.editor.getDocument().addUndoableEditListener(manager);
	    this.editor.registerKeyboardAction(new UndoAction(manager), KeyStroke.getKeyStroke(
	            KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
	    this.editor.registerKeyboardAction(new RedoAction(manager), KeyStroke.getKeyStroke(
	            KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
	    this.editor.addMouseListener(new PopClickListener());
	    this.editor.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	    
	    this.console = (JTextArea) this.findViewById("console");
	    this.redirectSystemStreams();
	    
	    this.line = (JLabel) this.findViewById("line");
	    
		JMenuItem newFile = (JMenuItem) this.findViewById("new");
		newFile.addActionListener(new NewFile());
		
		JMenuItem open = (JMenuItem) this.findViewById("open");
		open.addActionListener(new OpenFile());
		
		this.save = (JMenuItem) this.findViewById("save");
		this.save.addActionListener(new SaveFile());
		this.save.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, InputEvent.CTRL_MASK));
		
		this.saveAs = (JMenuItem) this.findViewById("saveAs");
		this.saveAs.addActionListener(new SaveAsFile());
		
		this.undo = (JMenuItem) this.findViewById("undo");
		this.undo.addActionListener(new UndoAction(manager));
		this.undo.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		
		this.redo = (JMenuItem) this.findViewById("redo");
		this.redo.addActionListener(new RedoAction(manager));
		this.redo.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Y, InputEvent.CTRL_MASK));
		
		JMenuItem cut = (JMenuItem) this.findViewById("cut");
		cut.addActionListener(new Cut());
		cut.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_X, InputEvent.CTRL_MASK));
		
		JMenuItem copy = (JMenuItem) this.findViewById("copy");
		copy.addActionListener(new Copy());
		copy.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_C, InputEvent.CTRL_MASK));
		
		JMenuItem paste = (JMenuItem) this.findViewById("paste");
		paste.addActionListener(new Paste());
		paste.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_V, InputEvent.CTRL_MASK));
		
		this.run = (JMenuItem) this.findViewById("run");
		this.run.addActionListener(new RunFile());
		
		JMenuItem about = (JMenuItem) this.findViewById("about");
		about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				About aboutDialog = new About();
				aboutDialog.show();
			}			
		});
		
		JMenuItem exit = (JMenuItem) this.findViewById("exit");
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Launcher.this.confirm();
				Launcher.this.killLayout();
			}
			
		});
	}
	
    class NewFile implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Launcher.this.confirm();
			Launcher.this.editor.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			currentFile = null;
			Launcher.this.frame.setTitle(Launcher.this.APP_NAME + " " + "Untitled");
		}
	}
		
	
	class OpenFile implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Launcher.this.confirm();
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new SimpleFileFilter(".xml"));
			int returnVal = fc.showOpenDialog(Launcher.this.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Launcher.this.currentFile = fc.getSelectedFile();
				try {
					FileReader in = new FileReader(currentFile);
					editor.read(in, null);
					in.close();
					frame.setTitle(Launcher.this.APP_NAME + " " + currentFile.getName());
					run.setEnabled(true);
					changed = false;
					saveAs.setEnabled(true);
				} catch (IOException ex) {
					//showError(ex, "Error reading file " + currentFile);
				}
			}
		}
	}
	
	class Cut implements ActionListener {
		@Override
    	public void actionPerformed(ActionEvent e) {
    		Launcher.this.editor.cut();
    	}
	}
	
	class Copy implements ActionListener {
		@Override
    	public void actionPerformed(ActionEvent e) {
    		Launcher.this.editor.copy();
    	}
	}
	
	class Paste implements ActionListener {
		@Override
    	public void actionPerformed(ActionEvent e) {
    		Launcher.this.editor.paste();
    	}
	}
		
	class SaveFile implements ActionListener {
			
		@Override
		public void actionPerformed(ActionEvent e) {
			Launcher.this.saveFile();
		}
	}
	
	class SaveAsFile implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Launcher.this.currentFile = null;
			Launcher.this.saveFile();
		}
	}
	
	class RunFile implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Launcher.this.confirm();
			if (currentFile == null) {
				return;
			}
			Launcher.this.console.setText("");
			MockApp mock = new MockApp(currentFile);
			mock.run();
		}
	}
		
	class EditorListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if(!changed)
			{
				Launcher.this.frame.setTitle("*" + Launcher.this.frame.getTitle());
			}
			Launcher.this.changed = true;
			Launcher.this.save.setEnabled(true);
			Launcher.this.saveAs.setEnabled(true);
			Launcher.this.undo.setEnabled(true);
			Launcher.this.run.setEnabled(true);
			try {
				line.setText("Line: " + getLineOfOffset(editor, editor.getCaretPosition()));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	class UndoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		public UndoAction(UndoManager manager) {
			this.manager = manager;
	    }

	    public void actionPerformed(ActionEvent evt) {
	    	try {
	    		manager.undo();
	    		Launcher.this.redo.setEnabled(true);
	    		if(!manager.canUndo())
	    			Launcher.this.undo.setEnabled(false);
	    	} catch (CannotUndoException e) {
	    		Toolkit.getDefaultToolkit().beep();
	        }
	    }
	    	

	    private UndoManager manager;
	}

	  // The Redo action
	class RedoAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RedoAction(UndoManager manager) {
			this.manager = manager;
	    }

	    public void actionPerformed(ActionEvent evt) {
	    	try {
	    		manager.redo();
	    		Launcher.this.undo.setEnabled(true);
	    		if(!manager.canRedo())
	    			Launcher.this.redo.setEnabled(false);
	    	} catch (CannotRedoException e) {
	    		Toolkit.getDefaultToolkit().beep();
	    	}
	    }

	    private UndoManager manager;
	}
	
	class PopClickListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e){
	    	int modifiers = e.getModifiers();
	        if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
	        	try {
	        		line.setText("Line: " + getLineOfOffset(editor, editor.getCaretPosition()));
			    } catch (BadLocationException e1) {
				    // TODO Auto-generated catch block
				    e1.printStackTrace();
			    }
	        }
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    public void mouseReleased(MouseEvent e){
	        if (e.isPopupTrigger())
	            doPop(e);
	    }

	    private void doPop(MouseEvent e) {
	        Popup p = new Popup();
	        JPopupMenu menu = (JPopupMenu) p.findViewById("popup");
	        
	        JMenuItem cut = (JMenuItem) p.findViewById("cut");
	        cut.addActionListener(new Cut());
	        
	        JMenuItem copy = (JMenuItem) p.findViewById("copy");
	        copy.addActionListener(new Copy());
	        
	        JMenuItem paste = (JMenuItem) p.findViewById("paste");
	        paste.addActionListener(new Paste());
	        menu.show(e.getComponent(), e.getX(), e.getY());
	    }
	    
	}
		
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Launcher l = new Launcher();
            	l.render(JFrame.DISPOSE_ON_CLOSE);
            }
		});
	}

}

class SimpleFileFilter extends FileFilter {

	  private String[] extensions;
	  private String description;

	  public SimpleFileFilter(String ext) {
		  this(new String[] { ext }, null);
	  }

	  public SimpleFileFilter(String[] exts, String descr) {
	      // Clone and lowercase the extensions
		  extensions = new String[exts.length];
		  for (int i = exts.length - 1; i >= 0; i--) {
			  extensions[i] = exts[i].toLowerCase();
		  }
	      // Make sure we have a valid (if simplistic) description
	      description = (descr == null ? exts[0] + " files" : descr);
	  }

	  public boolean accept(File f) {
	      // We always allow directories, regardless of their extension
	      if (f.isDirectory()) {
	          return true;
	      }
	      
	      // Ok, it's a regular file, so check the extension
	      String name = f.getName().toLowerCase();
	      for (int i = extensions.length - 1; i >= 0; i--) {
	    	  if (name.endsWith(extensions[i])) {
	    		  return true;
	    	  }
	      }
	      return false;
	  }

	  public String getDescription() {
		  return description;
	  }
}
