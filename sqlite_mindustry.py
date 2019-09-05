import sqlite3


def make_db(db):
    conn = sqlite3.connect(db)
    conn.execute('''CREATE TABLE IF NOT EXISTS login(id INTEGER,
                                                     login TEXT,
                                                     password TEXT,
                                                     ip TEXT,
                                                     uuid TEXT,
                                                     isAdmin INTEGER)''')
    conn.commit()
    conn.close()
    

def insert(db, discordid, login, password, isAdmin):
    try:
        conn = sqlite3.connect(db)    
        conn.execute('INSERT INTO login(id, login, password, isAdmin) VALUES(?,?,?,?)',
                     (discordid, login, password, isAdmin))
        conn.commit()
        succes = True
    except:
        succes = False
    finally:
        conn.close()
        return succes
        
           
def check(db, discordid, login):
    try:
        conn = sqlite3.connect(db)
        c = conn.cursor()
        c.execute('SELECT id FROM login WHERE id=? OR login=?', (discordid, login))
        num = len(c.fetchmany())
    except:
        num = 1
    finally:
        c.close()
        conn.close()
        return num
    

def get_data(db, discordid):
    try:
        conn = sqlite3.connect(db)
        c = conn.cursor()
        c.execute('SELECT login, password FROM login WHERE id=?', (discordid,))
        data = c.fetchmany()
    except:
        data = [('error', 'error')]
    finally:
        c.close()
        conn.close()
    return data if len(data) else [('error', 'error')]


def changeLogin(db, discordid, login, password):
    try:
        conn = sqlite3.connect(db)
        conn.execute('UPDATE login SET login=?, password=? WHERE id=?',
                     (login, password, discordid))
        conn.commit()
        success = True
    except:
        success = False
    finally:
        conn.close()
        return success
    

def delete(db, discordid):
    try:
        conn = sqlite3.connect(db)
        conn.execute('DELETE FROM login WHERE id=?', (discordid,))
        conn.commit()
        print('successfully deleted %s' %(discordid))
        success = True
    except:
        success = False
    finally:
        conn.close()
        return success
    

def changeAdmin(db, isAdmin, id):
    try:
        conn = sqlite3.connect(db)
        conn.execute("UPDATE login SET isAdmin = ? WHERE id = ?",
                     (isAdmin, id))
        conn.commit()
        print("successfully change adminstatus %s" %(id))
    finally:
        conn.close()
        



    