	package com.example.QuanLyGiaoDich.models;

	import jakarta.persistence.*;

	@Entity
	@Table(name = "BENEFICIARY")
	public class Beneficiary {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "BENEFICIARYID")
	    private Long beneficiaryID;

	    @Column(name = "USERID", length = 50)
	    private String userID;

	    @ManyToOne
	    @JoinColumn(name = "USERID", referencedColumnName = "USERID", insertable = false, updatable = false)
	    private Users user;

	    // Constructors, getters, and setters...

	    // Default constructor
	    public Beneficiary() {
	    }

	    // Parameterized constructor
	    public Beneficiary(String userID) {
	        this.userID = userID;
	    }

		public Long getBeneficiaryID() {
			return beneficiaryID;
		}

		public void setBeneficiaryID(Long beneficiaryID) {
			this.beneficiaryID = beneficiaryID;
		}

		public String getUserID() {
			return userID;
		}

		public void setUserID(String userID) {
			this.userID = userID;
		}

		public Users getUser() {
			return user;
		}

		public void setUser(Users user) {
			this.user = user;
		}

	    // Getters and setters...
	    
	}

