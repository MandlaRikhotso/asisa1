import { Component, OnInit } from '@angular/core';
import { AuthoriseService } from '../_services';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
version: any;
time: any;
  constructor(
    private authoriseService: AuthoriseService,
  ) { }

  ngOnInit() {
    this.authoriseService.getBuildVersion().subscribe(data => {
      this.version = data['version'];
      this.time = data['time'];
    });
  }

}
