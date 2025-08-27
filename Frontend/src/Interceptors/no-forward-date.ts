/**
 * Maximum date the user can choose, in this case now and in the past
 */
export class NoForwardDate {
    now = new Date()
    maxDate = this.now.toISOString().substring(0, 10);
    maxMonth = this.maxDate.substring(0, 7);

    constructor() {

        const dates = document.querySelectorAll('input[type="date"]')
        dates.forEach(date => {
            date.setAttribute("max", this.maxDate);
        })
        const months = document.querySelectorAll('input[type="month"]')
        months.forEach(month => {
            month.setAttribute("max", this.maxMonth);
        })
    }
}


export class NoForwardDateIncludeTommorrow {

  now = new Date();
  tomorrow = new Date(this.now.getTime() + (1000 * 60 * 60 * 24));
  maxDate = this.tomorrow.toISOString().substring(0, 10);
  maxMonth = this.maxDate.substring(0, 7);

  constructor() {

      const dates = document.querySelectorAll('input[type="date"]')
      dates.forEach(date => {
          date.setAttribute("max", this.maxDate);
      })
      const months = document.querySelectorAll('input[type="month"]')
      months.forEach(month => {
          month.setAttribute("max", this.maxMonth);
      })
  }
}







