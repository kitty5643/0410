package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.Attachment;
import edu.ouhk.comps380f.model.Message;
import edu.ouhk.comps380f.model.MessageReply;
import edu.ouhk.comps380f.model.MessageUser;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    @Autowired
    public static int key;
    private DataSource dataSource;
    private JdbcOperations jdbcOp;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcOp = new JdbcTemplate(this.dataSource);
    }

    private static final String SQL_INSERT_ENTRY
            = "insert into messageTopic (username, title, message, date, category) values (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_FILE2
            = "insert into attachmentTopic (file_name, mime, file_data, id_topic) values (?, ?, ?, ?)";

    @Override
    public void create(Message entry, MessageUser user) {
        Connection conn = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt = null;

        try {
            conn = dataSource.getConnection();

            stmt = conn.prepareStatement(SQL_INSERT_ENTRY, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, entry.getCustomerName());
            stmt.setString(2, entry.getSubject());
            stmt.setString(3, entry.getBody());
            stmt.setTimestamp(4, new Timestamp(entry.getDate().getTime()));
            stmt.setString(5, entry.getCategory());
            stmt.execute();
            ResultSet keys = stmt.getGeneratedKeys();
            key = 0;
            while (keys.next()) {

                key = keys.getInt(1);
            }
            for (int i = 0; i < entry.getAttachments().size(); i++) {
                stmt2 = conn.prepareStatement(SQL_INSERT_FILE2);
                Attachment attachment = entry.getAttachments().get(i);
                stmt2.setString(1, attachment.getName());
                stmt2.setString(2, attachment.getMimeContentType());
                stmt2.setBlob(3, new SerialBlob(attachment.getContents()));
                stmt2.setInt(4, key);
                stmt2.execute();
            }
            //jdbcOp.update(SQL_INSERT_FILE);
            //uploadFileTopic();
            //System.out.println("Key ID:" + keys);
        } catch (SQLException e) {
            // do something ... not sure what, though
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Even less sure about what to do here
            }
        }
    }

    private static final String SQL_SELECT_ALL_ENTRY
            = "select id, username, title, message, date from messageTopic where category = ?";

    @Override
    public List<Message> findAllTopic(String category) {

        /*String SQL_SELECT_ALL_ENTRY
        = "select id, username, title, message, date from messageTopic where category = ?";*/
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_ALL_ENTRY);
            stmt.setString(1, category);
            rs = stmt.executeQuery();
            List<Message> entries = new ArrayList<>();
            while (rs.next()) {
                Message entry = new Message();
                MessageUser entry2 = new MessageUser();
                entry.setId(rs.getInt("id"));
                entry.setCustomerName(rs.getString("username"));
                entry.setSubject(rs.getString("title"));
                entry.setBody(rs.getString("message"));
                entry.setDate(toDate(rs.getTimestamp("date")));
                entries.add(entry);
            }
            return entries;
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    MessageReply messageReply = new MessageReply();
    int num = 1;
    private String SQL_SELECT_ALL_ENTRY2
            = "select id, username, message, date from messageReply where id_topic = ?";

    @Override
    public List<MessageReply> findAllReply(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_ALL_ENTRY2);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            List<MessageReply> entries = new ArrayList<>();
            while (rs.next()) {
                MessageReply entry = new MessageReply();

                entry.setId(rs.getInt("id"));
                entry.setCustomerName(rs.getString("username"));
                entry.setBody(rs.getString("message"));
                //entry.setDate(toDate(rs.getTimestamp("date")));
                entries.add(entry);
            }
            return entries;
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    private static final String SQL_SELECT_ENTRY
            = "select id, username, title, message, date from messageTopic where id = ?";

    @Override
    public Message findById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_ENTRY);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Message entry = null;
            if (rs.next()) {
                entry = new Message();
                entry.setId(rs.getInt("id"));
                entry.setCustomerName(rs.getString("username"));
                entry.setSubject(rs.getString("title"));
                entry.setBody(rs.getString("message"));
                entry.setDate(toDate(rs.getTimestamp("date")));
            }
            return entry;
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return null;
    }

    private static final String SQL_UPDATE_ENTRY
            = "update message set title = ?, message = ?, date = ? where id = ?";

    @Override
    public void update(Message entry) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_ENTRY);
            stmt.setString(1, entry.getSubject());
            stmt.setString(2, entry.getBody());
            stmt.setTimestamp(3, new Timestamp(entry.getDate().getTime()));
            //stmt.setInt(4, entry.getId());
            stmt.execute();
        } catch (SQLException e) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public static Date toDate(Timestamp timestamp) {
        long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
        return new java.util.Date(milliseconds);
    }

    private static final String SQL_INSERT_ENTRY_REPLY
            = "insert into messageReply (username, message, id_topic, date) values (?,?, ?, ?)";

    @Override
    public void reply(int id, MessageReply entry, MessageUser user) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_ENTRY_REPLY);
            stmt.setString(1, entry.getCustomerName());
            stmt.setString(2, entry.getBody());
            stmt.setLong(3, id);
            stmt.setTimestamp(4, new Timestamp(entry.getDate().getTime()));
            stmt.execute();
        } catch (SQLException e) {
            // do something ... not sure what, though
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                // Even less sure about what to do here
            }
        }
    }

    //-------------------DELETE TOPIC AND REPLY-------------------------------------------
    private static final String SQL_DELETE_REPLY
            = "delete from messageReply where id_topic = ?";
    private static final String SQL_DELETE_TOPIC
            = "delete from messageTopic where id = ?";

    @Override
    public void deleteTopic(int id) {
        jdbcOp.update(SQL_DELETE_REPLY, id); //must delete first (foreign key)
        jdbcOp.update(SQL_DELETE_TOPIC, id);
    }

    private static final String SQL_DELETE_REPLY2
            = "delete from messageReply where id = ?";

    @Override
    public void deleteReply(int id) {
        jdbcOp.update(SQL_DELETE_REPLY2, id); //must delete first (foreign key)
    }

    //---------------------EDIT TOPIC-------------------------------------------
    private static final String SQL_UPDATE_ENTRY_TOPIC
            = "update messageTopic set title = ?, message = ?, date = ? where id = ?";

    public void updateTopic(Message entry) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_ENTRY_TOPIC);
            stmt.setString(1, entry.getSubject());
            stmt.setString(2, entry.getBody());
            stmt.setTimestamp(3, new Timestamp(entry.getDate().getTime()));
            stmt.setInt(4, (int) entry.getId());
            stmt.execute();
        } catch (SQLException e) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    //-------------------EDIT REPLY---------------------------------------------
    private static final String SQL_UPDATE_ENTRY_REPLY
            = "update messageReply set message = ?, date = ? where id = ?";

    public void updateReply(MessageReply entry) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_ENTRY_REPLY);
            stmt.setString(1, entry.getBody());
            stmt.setTimestamp(2, new Timestamp(entry.getDate().getTime()));
            stmt.setInt(3, (int) entry.getId());
            stmt.execute();
        } catch (SQLException e) {
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    //--------------------UPLOAD ATTACHMENT----------------------------------------------
    private static final String SQL_INSERT_ENTRY_FILETOPIC
            = "insert into attachmentTopic (file_name, file_data, mime, id_topic) values (?, ?, ?, ?)";

    public void uploadFileTopic() {
        /*  Connection conn = null;
        PreparedStatement stmt2 = null;
            try{
                conn = dataSource.getConnection();
            stmt2 = conn.prepareStatement("insert into attachmentTopic(file_name,  mime, id_topic) values('kk', '.doc', 1)");
            //stmt.setInt(1, key);
            stmt2.execute();
            }catch (SQLException e) {
            // do something ... not sure what, though
        } */
    }

    private static final String SQL_SELECT_ENTRY_FINDATTACHMENT_TOPIC
            = "select * from attachmentTopic where id_topic = ?";

    @Override
    public List<Attachment> findAttachmentTopicById(int id) {
        //Attachment att = new Attachment();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_ENTRY_FINDATTACHMENT_TOPIC);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Attachment entry = null;
            List<Attachment> entries = new ArrayList<>();
            Message message = new Message();
            while (rs.next()) {
                entry = new Attachment();
                entry.setName(rs.getString("file_name"));
                entry.setMimeContentType(rs.getString("mime"));
                Blob blob = rs.getBlob("file_data");
                int blobLength = (int) blob.length();
                byte[] blobAsBytes = blob.getBytes(1, blobLength);
                entry.setContents(blobAsBytes);
                entry.setMessageId(rs.getInt("id_topic"));
                entries.add(entry);
                
            }message.setAttachments(entries);
            return entries;

        } catch (SQLException e) {
        }

        return null;
    }

    private static final String SQL_UPDATE_ENTRY_DELETEATTACHMENT_TOPIC
            = "select * from attachmentTopic where id_topic = ?";
    
    @Override
    public void deleteAttachment(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
