package managedbeansv3;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import entities.Usuario;

public class SessionManager implements Serializable{
    
    public void addSession(Usuario user){
FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(user.getLogin(), user);
    }
    public void delSession(){
FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
    }
    
}
