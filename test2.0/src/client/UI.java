package client;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class UI extends InGame{
	
	public UI(PlayerMob plyr) {
	}
	
	private JTextArea incoming;
	private JTextField outgoing;
	private JPanel chatPanel;
	private JPanel chatPanelSection;
	private JButton sendButton;
	private JScrollPane qScroller;

	public void chat(JPanel panel){

		chatPanel = new JPanel();
		chatPanelSection = new JPanel();
		
		incoming = new JTextArea(5,49);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)incoming.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		outgoing = new JTextField(45);
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());

		chatPanel.setLayout( new BoxLayout( chatPanel, BoxLayout.Y_AXIS ) );
		chatPanel.add(qScroller);
		chatPanelSection.add(outgoing);
		chatPanelSection.add(sendButton);
		chatPanel.add(chatPanelSection);
	
	}
	
	public JPanel getChatPanel(){
		return chatPanel;
	}
	
	public JTextArea getIncoming(){
		return incoming;
	}
	
	public JTextField getOutgoing(){
		return outgoing;
	}

}
