package FOS.ManagedBean;
import FOS.Entity.ChecklistItem;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@ManagedBean
public class CustomizedDocumentsViewManagedBean implements Serializable {
     
    private List<ChecklistItem> items;
    String checklistName;
    Long scheduleId;
    String comments;
         
    @ManagedProperty("#{checklistManagedBean}")
    private ChecklistManagedBean checkBean;
     
    @PostConstruct
    public void init() {
        items = checkBean.getChecklistItemsForChecklist();
        setChecklistName(checkBean.getChecklistName());      
        setScheduleId(checkBean.getScheduleId());
        setComments(checkBean.getComments());
    }
 
    public List<ChecklistItem> getItems() {
        return items;
    }
     
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        setComments(checkBean.getComments());
        String titleOfPage = checklistName + " checklist for Schedule Id: " + scheduleId +"\n\n\n";
        
        String commentsOfChecklist = "Comments: \n" + comments + "\n\n\n";
        
        Document pdf = (Document) document;
        pdf.open();
        Paragraph title = new Paragraph (titleOfPage);
        title.setAlignment("Center");
        pdf.add(title);
        
        Paragraph secondSection = new Paragraph (commentsOfChecklist);
        secondSection.setAlignment("Center");
        pdf.add(secondSection);
        
        pdf.setPageSize(PageSize.A4);

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        //String logo = servletContext.getRealPath("") + File.separator + "resources" +"images" + File.separator + "pdf.png";         
        //pdf.add(Image.getInstance(logo));
         
    }

    public ChecklistManagedBean getCheckBean() {
        return checkBean;
    }

    public void setCheckBean(ChecklistManagedBean checkBean) {
        this.checkBean = checkBean;
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    
    
}
