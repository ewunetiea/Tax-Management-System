export class DateFormat {
    dateFormatter(date: any): any {
       let  dateformat: Date=new Date();
        let d: any = date;
        dateformat = new Date(d);
        let day = dateformat.toString();
        let dayStore: any = '';
        for (let index = 0; index < 4; index++) {
          if (index == 0) {
            dayStore = dayStore + day.split(' ')[index];
            dayStore = dayStore + ', '
          }
          else {
            dayStore = dayStore + ' ' + day.split(' ')[index];
          }
        }
        return dayStore;
      }
      
}
