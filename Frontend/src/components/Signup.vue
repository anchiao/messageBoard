<template>
  <div>
    <form v-if="!signed" class="form-horizontal">
      <fieldset>
        <div id="legend">
          <legend class="">Register</legend>
        </div>
        <div class="control-group">
          <label class="control-label" for="username">Username</label>
          <div class="controls">
            <input
              v-model="user.username"
              type="text"
              id="username"
              name="username"
              placeholder=""
              class="input-xlarge"
            />
            <p class="help-block">
              {{usernameText}}
            </p>
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="email">E-mail</label>
          <div class="controls">
            <input
              v-model="user.email"
              type="text"
              id="email"
              name="email"
              placeholder=""
              class="input-xlarge"
            />
            <p class="help-block">Please provide your E-mail</p>
          </div>
        </div>

        <div class="control-group">
          <label class="control-label" for="password">Password</label>
          <div class="controls">
            <input
              v-model="user.password"
              type="password"
              id="password"
              name="password"
              placeholder=""
              class="input-xlarge"
            />
            <p class="help-block">Password should be at least 4 characters</p>
          </div>
        </div>

        <div class="control-group">
          <!-- Button -->
          <div class="controls">
            <a @click="register" class="btn btn-success">Register</a>
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script type="text/javascript">
export default {
  name: "sign-up",
  data() {
    return {
      signed: false,
      usernameText: "Username can contain any letters or numbers, without spaces",
      user: {
        username: "",
        password: "",
        email: "",
      },
    };
  },
  methods: {
    register() {
      let url = "http://localhost:8081/signup";

      this.$http.post(url, this.user).then(
        (res) => {
          console.log(JSON.stringify(res)); //can't see res unless stringify or assign to local variable.
          if (res.hasOwnProperty("body")) {
            if (res.body.HttpStatus == "OK") {
              console.log("equals!");
              this.user.username = res.body.username;
              console.log("username assigned = " + this.user.username);
              this.signed = true;
              localStorage.setItem("loggedIn", "true");
              localStorage.setItem("username", this.user.username);
              location.assign('http://localhost:8080');
            } else {
              this.usernameText = "Username exsists, please choose another one";              
            }
          }
        },
        (err) => {
          console.log("exception:" + err);
        }
      );
    },
  },
};
</script>
