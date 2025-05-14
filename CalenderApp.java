/* 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.time.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class CalendarApp { public static void main(String[] args) { SwingUtilities.invokeLater(() -> new CalendarFrame()); } }

class CalendarFrame extends JFrame { private JPanel calendarPanel; private JLabel monthLabel; private LocalDate currentDate; private Map<LocalDate, List<Reminder>> reminders;

public CalendarFrame() {
    setTitle("Calendar with Reminders");
    setSize(600, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    reminders = ReminderManager.loadReminders();
    currentDate = LocalDate.now();

    monthLabel = new JLabel("", SwingConstants.CENTER);
    JButton prevBtn = new JButton("<");
    JButton nextBtn = new JButton(">");

    prevBtn.addActionListener(e -> {
        currentDate = currentDate.minusMonths(1);
        refreshCalendar();
    });

    nextBtn.addActionListener(e -> {
        currentDate = currentDate.plusMonths(1);
        refreshCalendar();
    });

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(prevBtn, BorderLayout.WEST);
    topPanel.add(monthLabel, BorderLayout.CENTER);
    topPanel.add(nextBtn, BorderLayout.EAST);

    calendarPanel = new JPanel(new GridLayout(0, 7));

    add(topPanel, BorderLayout.NORTH);
    add(calendarPanel, BorderLayout.CENTER);

    refreshCalendar();
    setVisible(true);
}

private void refreshCalendar() {
    calendarPanel.removeAll();
    monthLabel.setText(currentDate.getMonth() + " " + currentDate.getYear());

    LocalDate firstDay = currentDate.withDayOfMonth(1);
    int startDay = firstDay.getDayOfWeek().getValue() % 7; // Sunday = 0
    int daysInMonth = currentDate.lengthOfMonth();

    for (int i = 0; i < startDay; i++) {
        calendarPanel.add(new JLabel(""));
    }

    for (int day = 1; day <= daysInMonth; day++) {
        LocalDate date = currentDate.withDayOfMonth(day);
        JButton dayBtn = new JButton(String.valueOf(day));

        if (reminders.containsKey(date)) {
            dayBtn.setBackground(Color.YELLOW);
        }

        dayBtn.addActionListener(e -> {
            new ReminderDialog(this, date, reminders);
            ReminderManager.saveReminders(reminders);
            refreshCalendar();
        });

        calendarPanel.add(dayBtn);
    }

    calendarPanel.revalidate();
    calendarPanel.repaint();
}

}

class ReminderDialog extends JDialog { public ReminderDialog(JFrame parent, LocalDate date, Map<LocalDate, List<Reminder>> reminders) { super(parent, "Reminders for " + date, true); setLayout(new BorderLayout());

JTextArea reminderArea = new JTextArea(10, 30);
    List<Reminder> dayReminders = reminders.getOrDefault(date, new ArrayList<>());

    for (Reminder r : dayReminders) {
        reminderArea.append("- " + r.title + ": " + r.description + "\n");
    }

    JTextField titleField = new JTextField(20);
    JTextField descField = new JTextField(20);
    JButton addBtn = new JButton("Add Reminder");

    addBtn.addActionListener(e -> {
        String title = titleField.getText();
        String desc = descField.getText();
        if (!title.isEmpty()) {
            dayReminders.add(new Reminder(title, desc));
            reminders.put(date, dayReminders);
            dispose();
        }
    });

    JPanel inputPanel = new JPanel();
    inputPanel.add(new JLabel("Title:"));
    inputPanel.add(titleField);
    inputPanel.add(new JLabel("Desc:"));
    inputPanel.add(descField);
    inputPanel.add(addBtn);

    add(new JScrollPane(reminderArea), BorderLayout.CENTER);
    add(inputPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(parent);
    setVisible(true);
}

}

class Reminder implements Serializable { String title; String description;

public Reminder(String title, String description) {
    this.title = title;
    this.description = description;
}

}

class ReminderManager { private static final String FILE_NAME = "reminders.ser";

@SuppressWarnings("unchecked")
public static Map<LocalDate, List<Reminder>> loadReminders() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
        return (Map<LocalDate, List<Reminder>>) ois.readObject();
    } catch (Exception e) {
        return new HashMap<>();
    }
}

public static void saveReminders(Map<LocalDate, List<Reminder>> reminders) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
        oos.writeObject(reminders);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.util.*;
import java.io.*;
import java.util.List;

public class CalendarApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalendarFrame());
    }
}

class CalendarFrame extends JFrame {
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private LocalDate currentDate;
    private Map<LocalDate, List<Reminder>> reminders;

    public CalendarFrame() {
        setTitle("Calendar with Reminders");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        reminders = ReminderManager.loadReminders();
        currentDate = LocalDate.now();

        monthLabel = new JLabel("", SwingConstants.CENTER);
        JButton prevBtn = new JButton("<");
        JButton nextBtn = new JButton(">");

        prevBtn.addActionListener(e -> {
            currentDate = currentDate.minusMonths(1);
            refreshCalendar();
        });

        nextBtn.addActionListener(e -> {
            currentDate = currentDate.plusMonths(1);
            refreshCalendar();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(prevBtn, BorderLayout.WEST);
        topPanel.add(monthLabel, BorderLayout.CENTER);
        topPanel.add(nextBtn, BorderLayout.EAST);

        calendarPanel = new JPanel(new GridLayout(0, 7));

        add(topPanel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);

        refreshCalendar();
        setVisible(true);
    }

    private void refreshCalendar() {
        calendarPanel.removeAll();
        monthLabel.setText(currentDate.getMonth() + " " + currentDate.getYear());

        // Day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : days) {
            calendarPanel.add(new JLabel(dayName, SwingConstants.CENTER));
        }

        LocalDate firstDay = currentDate.withDayOfMonth(1);
        int startDay = firstDay.getDayOfWeek().getValue();
        startDay = (startDay == 7) ? 0 : startDay; // Make Sunday = 0

        int daysInMonth = currentDate.lengthOfMonth();

        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentDate.withDayOfMonth(day);
            JButton dayBtn = new JButton(String.valueOf(day));

            if (date.equals(LocalDate.now())) {
                dayBtn.setBackground(Color.CYAN); // Highlight today
            }

            if (reminders.containsKey(date)) {
                dayBtn.setBackground(Color.YELLOW);

                // Tooltip with reminders
                List<Reminder> dayReminders = reminders.get(date);
                StringBuilder tooltip = new StringBuilder("<html>");
                for (Reminder r : dayReminders) {
                    tooltip.append("• ").append(r.title).append("<br>");
                }
                tooltip.append("</html>");
                dayBtn.setToolTipText(tooltip.toString());
            }

            dayBtn.addActionListener(e -> {
                new ReminderDialog(this, date, reminders);
                ReminderManager.saveReminders(reminders);
                refreshCalendar();
            });

            calendarPanel.add(dayBtn);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}

class ReminderDialog extends JDialog {
    public ReminderDialog(JFrame parent, LocalDate date, Map<LocalDate, List<Reminder>> reminders) {
        super(parent, "Reminders for " + date, true);
        setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Reminder> dayReminders = reminders.getOrDefault(date, new ArrayList<>());
        JList<String> reminderList = new JList<>(listModel);

        for (Reminder r : dayReminders) {
            listModel.addElement(r.title + ": " + r.description);
        }

        JScrollPane scrollPane = new JScrollPane(reminderList);

        JTextField titleField = new JTextField(10);
        JTextField descField = new JTextField(10);
        JButton addBtn = new JButton("Add");
        JButton deleteBtn = new JButton("Delete Selected");

        addBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descField.getText().trim();
            if (!title.isEmpty()) {
                dayReminders.add(new Reminder(title, desc));
                reminders.put(date, dayReminders);
                listModel.addElement(title + ": " + desc);
                titleField.setText("");
                descField.setText("");
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedIndex = reminderList.getSelectedIndex();
            if (selectedIndex != -1) {
                dayReminders.remove(selectedIndex);
                listModel.remove(selectedIndex);
                if (dayReminders.isEmpty()) {
                    reminders.remove(date);
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Desc:"));
        inputPanel.add(descField);
        inputPanel.add(addBtn);
        inputPanel.add(deleteBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}

class Reminder implements Serializable {
    String title;
    String description;

    public Reminder(String title, String description) {
        this.title = title;
        this.description = description;
    }
}

class ReminderManager {
    private static final String FILE_NAME = "reminders.ser";

    @SuppressWarnings("unchecked")
    public static Map<LocalDate, List<Reminder>> loadReminders() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (Map<LocalDate, List<Reminder>>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static void saveReminders(Map<LocalDate, List<Reminder>> reminders) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(reminders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
